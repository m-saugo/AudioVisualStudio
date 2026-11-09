import { useEffect, useMemo, useState } from "react";
import "./App.css";
import MenuLateral from "./components/MenuLateral";

const API = import.meta.env.DEV
  ? (import.meta.env.VITE_API_URL || "/api")
  : "";

console.log("VALOR DA API:", API);
console.log("AMBIENTE DEV:", import.meta.env.DEV);

  const CADASTROS = {
    Atores: {
      endpoint: "/atores",
      id: "id",
      singular: "ator",
      plural: "atores",
      campos: [
        { name: "nome", label: "Nome completo", placeholder: "Ex.: Maria da Silva", required: true },
        { name: "nacionalidade", label: "Nacionalidade", placeholder: "Ex.: Brasileira", required: true },
        { name: "idade", label: "Idade", type: "number", placeholder: "Ex.: 25", required: true },
        { name: "cpf", label: "CPF", placeholder: "Ex.: 000.000.000-00", required: true },
        { name: "cache", label: "Cachê (R$)", type: "number", placeholder: "Ex.: 5000.00", required: true, step: "0.01" },
        { name: "papel", label: "Papel", placeholder: "Ex.: Protagonista", required: true },
        { name: "tipoPersonagem", label: "Tipo de personagem", placeholder: "Ex.: Principal", required: true },
      ],
      colunas: ["nome", "cpf", "nacionalidade", "idade", "cache", "papel", "tipoPersonagem"],
      titulos: ["Nome", "CPF", "Nacionalidade", "Idade", "Cachê", "Papel", "Tipo de personagem"],
    },

    Diretores: {
      endpoint: "/diretores",
      id: "id",
      singular: "diretor",
      plural: "diretores",
      campos: [
        { name: "nome", label: "Nome completo", placeholder: "Ex.: Ana Martins", required: true },
        { name: "nacionalidade", label: "Nacionalidade", placeholder: "Ex.: Brasileira", required: true },
        { name: "idade", label: "Idade", type: "number", placeholder: "Ex.: 40", required: true },
        { name: "cpf", label: "CPF", placeholder: "Ex.: 000.000.000-00", required: true },
        { name: "salario", label: "Salário (R$)", type: "number", placeholder: "Ex.: 12000.00", required: true, step: "0.01" },
        { name: "estiloDirecao", label: "Estilo de direção", placeholder: "Ex.: Drama", required: true },
      ],
      colunas: ["nome", "cpf", "nacionalidade", "idade", "salario", "estiloDirecao"],
      titulos: ["Nome", "CPF", "Nacionalidade", "Idade", "Salário", "Estilo de direção"],
    },

    Roteiristas: {
      endpoint: "/roteiristas",
      id: "id",
      singular: "roteirista",
      plural: "roteiristas",
      campos: [
        { name: "nome", label: "Nome completo", placeholder: "Ex.: Pedro Santos", required: true },
        { name: "nacionalidade", label: "Nacionalidade", placeholder: "Ex.: Brasileiro", required: true },
        { name: "idade", label: "Idade", type: "number", placeholder: "Ex.: 35", required: true },
        { name: "cpf", label: "CPF", placeholder: "Ex.: 000.000.000-00", required: true },
        { name: "salario", label: "Salário (R$)", type: "number", placeholder: "Ex.: 8000.00", required: true, step: "0.01" },
        { name: "especialidade", label: "Especialidade", placeholder: "Ex.: Roteiros de ficção", required: true },
      ],
      colunas: ["nome", "cpf", "nacionalidade", "idade", "salario", "especialidade"],
      titulos: ["Nome", "CPF", "Nacionalidade", "Idade", "Salário", "Especialidade"],
    },

    Filmes: {
      endpoint: "/filmes",
      id: "codigo",
      singular: "filme",
      plural: "filmes",
      campos: [
        { name: "titulo", label: "Título", placeholder: "Ex.: Além das Estrelas", required: true },
        { name: "genero", label: "Gênero", placeholder: "Ex.: Drama, Romance, Suspense", required: true },
        { name: "duracao", label: "Duração (minutos)", type: "number", placeholder: "Ex.: 120", required: true },
      ],
      colunas: ["codigo", "titulo", "genero", "duracao"],
      titulos: ["Código", "Título", "Gênero", "Duração"],
    },
  };


const PAGINAS_ADMIN = ["Visão geral", "Atores", "Diretores", "Roteiristas", "Filmes", "Atribuições", "Relatórios"];
const PAGINAS_PROFISSIONAL = ["Visão geral", "Minha conta"];

function criarFormulario(campos) {
  return Object.fromEntries(campos.map((campo) => [campo.name, ""]));
}

function formatarValor(campo, valor) {
  if (valor === null || valor === undefined || valor === "") return "—";
  if (["cache", "salario"].includes(campo)) {
    return Number(valor).toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
  }
  if (campo === "duracao") return `${valor} min`;
  return String(valor);
}

function mensagemDoErro(dados, status) {
  if (dados && typeof dados === "object") {
    if (dados.erro) return dados.erro;
    if (dados.message) return dados.message;
    if (dados.mensagem) return dados.mensagem;
  }
  if (status === 401) return "Sua sessão não está ativa. Entre novamente.";
  if (status === 403) return "Você não tem permissão para realizar esta ação.";
  if (status === 404) return "O registro solicitado não foi encontrado.";
  return `Não foi possível concluir a solicitação (erro ${status}).`;
}

async function obterTokenCsrf() {
  const resposta = await fetch(`${API}/csrf`, { credentials: "include" });
  if (!resposta.ok) throw new Error("Não foi possível obter o token de segurança do servidor.");
  return resposta.json();
}

async function api(path, options = {}) {
  const method = (options.method || "GET").toUpperCase();
  const headers = { ...(options.headers || {}) };

  if (options.body !== undefined) headers["Content-Type"] = "application/json";

  if (!["GET", "HEAD", "OPTIONS"].includes(method)) {
    const csrf = await obterTokenCsrf();
    if (csrf?.headerName && csrf?.token) headers[csrf.headerName] = csrf.token;
  }

  const resposta = await fetch(`${API}${path}`, {
    ...options,
    method,
    headers,
    credentials: "include",
  });

  const texto = await resposta.text();
  let dados = null;
  if (texto) {
    try {
      dados = JSON.parse(texto);
    } catch {
      dados = texto;
    }
  }

  if (!resposta.ok) throw new Error(mensagemDoErro(dados, resposta.status));
  return dados;
}


  function App() {
    const [usuario, setUsuario] = useState(null);
    const [pagina, setPagina] = useState("Visão geral");
    const [dados, setDados] = useState({ Atores: [], Diretores: [], Roteiristas: [], Filmes: [] });
    const [busca, setBusca] = useState("");
    const [carregando, setCarregando] = useState(false);
    const [erro, setErro] = useState("");
    const [sucesso, setSucesso] = useState("");
    const [modoAcesso, setModoAcesso] = useState("login");
    const [login, setLogin] = useState({ email: "", senha: "", entrarComo: "ADMINISTRADOR" });
    const [cadastro, setCadastro] = useState({
      email: "", senha: "", tipoUsuario: "ATOR", nome: "", nacionalidade: "", idade: "", cpf: "",
      cache: "", papel: "", tipoPersonagem: "", salario: "", estiloDirecao: "", especialidade: "",
    });

    // MODO DE DEMONSTRAÇÃO
    const [modoDemo, setModoDemo] = useState(false);

    const dadosDemo = {
      Atores: [
        {
          id: 1,
          nome: "Mariana Costa",
          nacionalidade: "Brasileira",
          idade: 28,
          cpf: "000.000.000-00",
          cache: 5000,
          papel: "Protagonista",
          tipoPersonagem: "Principal",
        },
        {
          id: 2,
          nome: "Lucas Almeida",
          nacionalidade: "Brasileiro",
          idade: 32,
          cpf: "111.111.111-11",
          cache: 3500,
          papel: "Coadjuvante",
          tipoPersonagem: "Secundário",
        },
      ],

      Diretores: [
        {
          id: 1,
          nome: "Ana Martins",
          nacionalidade: "Brasileira",
          idade: 40,
          cpf: "222.222.222-22",
          salario: 12000,
          estiloDirecao: "Drama",
        },
      ],

      Roteiristas: [
        {
          id: 1,
          nome: "Pedro Santos",
          nacionalidade: "Brasileiro",
          idade: 35,
          cpf: "333.333.333-33",
          salario: 8000,
          especialidade: "Roteiros de ficção",
        },
      ],

      Filmes: [
        {
          codigo: 1,
          titulo: "Além das Estrelas",
          genero: "Drama",
          duracao: 120,
        },
        {
          codigo: 2,
          titulo: "O Último Verão",
          genero: "Suspense",
          duracao: 105,
        },
        {
          codigo: 3,
          titulo: "Caminhos",
          genero: "Romance",
          duracao: 95,
        },
      ],
    };

    const [formularioAberto, setFormularioAberto] = useState(false);
    const [registroEditado, setRegistroEditado] = useState(null);
    const [formulario, setFormulario] = useState({});
    const [perfil, setPerfil] = useState(null);
    const [filmesDoProfissional, setFilmesDoProfissional] = useState([]);
    const [codigoFilme, setCodigoFilme] = useState("");
    const [tipoVinculo, setTipoVinculo] = useState("atores");
    const [idProfissional, setIdProfissional] = useState("");
    const [equipe, setEquipe] = useState({ atores: [], diretores: [], roteiristas: [] });
    const [vinculosDemo, setVinculosDemo] = useState({});
    const [relatorio, setRelatorio] = useState(null);

    const administrador = usuario?.tipoUsuario === "ADMINISTRADOR";
    const paginas = administrador ? PAGINAS_ADMIN : PAGINAS_PROFISSIONAL;
    const configuracao = CADASTROS[pagina];

    function avisar(texto, tipo = "sucesso") {
      setErro(tipo === "erro" ? texto : "");
      setSucesso(tipo === "sucesso" ? texto : "");
    }

    function entrarModoDemo() {
      setModoDemo(true);

      setDados({
        Atores: [...dadosDemo.Atores],
        Diretores: [...dadosDemo.Diretores],
        Roteiristas: [...dadosDemo.Roteiristas],
        Filmes: [...dadosDemo.Filmes],
      });

      setVinculosDemo({});
      setCodigoFilme("");
      setIdProfissional("");
      setEquipe({ atores: [], diretores: [], roteiristas: [] });

      setUsuario({
        idUsuario: 0,
        email: "demo@audiovisualstudio.com",
        tipoUsuario: "ADMINISTRADOR",
        nome: "Administrador (Demonstração)",
      });

      setPagina("Visão geral");
      setErro("");
      setSucesso("Você entrou no modo de demonstração!");
    }


  async function carregarCadastros() {
    setCarregando(true);
    try {
      const nomes = Object.keys(CADASTROS);
      const resultados = await Promise.all(
        nomes.map(async (nome) => [nome, await api(CADASTROS[nome].endpoint)])
      );
      setDados(Object.fromEntries(resultados.map(([nome, lista]) => [nome, Array.isArray(lista) ? lista : []])));
    } catch (e) {
      avisar(e.message, "erro");
    } finally {
      setCarregando(false);
    }
  }

  async function carregarPerfil() {
    try {
      const resposta = await api("/minha-conta");
      setPerfil(resposta);

      if (resposta.tipoUsuario === "ATOR" && resposta.idProfissional) {
        await carregarFilmesDoProfissional(resposta.idProfissional);
      } else {
        setFilmesDoProfissional([]);
      }
    } catch (e) {
      avisar(e.message, "erro");
    }
  }

  async function carregarFilmesDoProfissional(id) {
    try {
      const filmes = await api(`/filmes/por-ator/${id}`);
      setFilmesDoProfissional(Array.isArray(filmes) ? filmes : []);
    } catch (e) {
      setFilmesDoProfissional([]);
      avisar(e.message, "erro");
    }
  }

  
    useEffect(() => {
    if (!usuario || modoDemo) return;

    if (administrador) carregarCadastros();
    else carregarPerfil();

  }, [usuario, modoDemo]);


  async function fazerLogin(evento) {
    evento.preventDefault();
    avisar("");
    setCarregando(true);
    try {
      const resposta = await api("/login", {
        method: "POST",
        body: JSON.stringify(login),
      });
      setUsuario(resposta);
      setPagina("Visão geral");
      setBusca("");
      avisar("Login realizado com sucesso!");
    } catch (e) {
      avisar(e.message, "erro");
    } finally {
      setCarregando(false);
    }
  }

  async function fazerCadastro(evento) {
    evento.preventDefault();
    avisar("");
    setCarregando(true);
    const profissional = cadastro.tipoUsuario !== "ADMINISTRADOR";
    const payload = {
      email: cadastro.email.trim(),
      senha: cadastro.senha,
      tipoUsuario: cadastro.tipoUsuario,
    };

    if (profissional) {
      Object.assign(payload, {
        nome: cadastro.nome,
        nacionalidade: cadastro.nacionalidade,
        idade: Number(cadastro.idade || 0),
        cpf: cadastro.cpf,
      });
      if (cadastro.tipoUsuario === "ATOR") {
        payload.cache = Number(cadastro.cache || 0);
        payload.papel = cadastro.papel;
        payload.tipoPersonagem = cadastro.tipoPersonagem;
      }
      if (["DIRETOR", "ROTEIRISTA"].includes(cadastro.tipoUsuario)) {
        payload.salario = Number(cadastro.salario || 0);
      }
      if (cadastro.tipoUsuario === "DIRETOR") payload.estiloDirecao = cadastro.estiloDirecao;
      if (cadastro.tipoUsuario === "ROTEIRISTA") payload.especialidade = cadastro.especialidade;
    }

    try {
      await api("/cadastro", { method: "POST", body: JSON.stringify(payload) });
      setLogin({ email: cadastro.email, senha: cadastro.senha, entrarComo: profissional ? "PROFISSIONAL" : "ADMINISTRADOR" });
      setModoAcesso("login");
      avisar("Cadastro realizado! Agora você pode entrar.");
    } catch (e) {
      avisar(e.message, "erro");
    } finally {
      setCarregando(false);
    }
  }

    async function sair() {
    if (modoDemo) {
      setUsuario(null);
      setPerfil(null);
      setDados({
        Atores: [],
        Diretores: [],
        Roteiristas: [],
        Filmes: [],
      });
      setVinculosDemo({});
      setCodigoFilme("");
      setIdProfissional("");
      setEquipe({ atores: [], diretores: [], roteiristas: [] });
      setRelatorio(null);
      setModoDemo(false);
      setFormularioAberto(false);
      setRegistroEditado(null);
      setFormulario({});
      setPagina("Visão geral");
      setModoAcesso("login");
      setErro("");
      setSucesso("Sessão de demonstração encerrada.");
      return;
    }

    try {
      await api("/login/sair", { method: "POST" });

      setUsuario(null);
      setPerfil(null);
      setDados({
        Atores: [],
        Diretores: [],
        Roteiristas: [],
        Filmes: [],
      });
      setVinculosDemo({});
      setCodigoFilme("");
      setIdProfissional("");
      setEquipe({ atores: [], diretores: [], roteiristas: [] });
      setRelatorio(null);
      setModoDemo(false);
      setFormularioAberto(false);
      setRegistroEditado(null);
      setFormulario({});
      setPagina("Visão geral");
      setModoAcesso("login");
      setErro("");
      setSucesso("");
    } catch (erro) {
      setErro(erro.message);
    }
  }

  function abrirPagina(nome) {
    setPagina(nome);
    setBusca("");
    setFormularioAberto(false);
    setRegistroEditado(null);
    setRelatorio(null);
    setErro("");
    setSucesso("");
  }

  function novoRegistro() {
    setRegistroEditado(null);
    setFormulario(criarFormulario(configuracao.campos));
    setFormularioAberto(true);
  }

  function editarRegistro(item) {
    const valores = criarFormulario(configuracao.campos);
    configuracao.campos.forEach((campo) => {
      valores[campo.name] = item[campo.name] ?? "";
    });
    setFormulario(valores);
    setRegistroEditado(item[configuracao.id]);
    setFormularioAberto(true);
  }

  
  
  async function salvarRegistro(evento) {
    evento.preventDefault();

    const payload = { ...formulario };

    configuracao.campos.forEach((campo) => {
      if (campo.type === "number") {
        payload[campo.name] = Number(payload[campo.name] || 0);
      }
    });

    const id = registroEditado;

    if (modoDemo) {
      setDados((dadosAtuais) => {
        const listaAtual = dadosAtuais[pagina] || [];

        if (id !== null) {
          const atualizados = listaAtual.map((item) =>
            item[configuracao.id] === id
              ? { ...item, ...payload }
              : item
          );

          return {
            ...dadosAtuais,
            [pagina]: atualizados,
          };
        }

        const novoId =
          Math.max(
            0,
            ...listaAtual.map(
              (item) => Number(item[configuracao.id]) || 0
            )
          ) + 1;

        return {
          ...dadosAtuais,
          [pagina]: [
            ...listaAtual,
            { ...payload, [configuracao.id]: novoId },
          ],
        };
      });

      avisar(
        id !== null
          ? "Cadastro atualizado com sucesso."
          : "Cadastro realizado com sucesso."
      );

      setFormularioAberto(false);
      setRegistroEditado(null);
      return;
    }

    const caminho =
      id !== null
        ? `${configuracao.endpoint}/${id}`
        : configuracao.endpoint;

    setCarregando(true);
    avisar("");

    try {
      await api(caminho, {
        method: id !== null ? "PUT" : "POST",
        body: JSON.stringify(payload),
      });

      await carregarCadastros();
      setFormularioAberto(false);
      setRegistroEditado(null);

      avisar(
        id !== null
          ? "Cadastro atualizado."
          : "Cadastro realizado com sucesso."
      );
    } catch (e) {
      avisar(e.message, "erro");
    } finally {
      setCarregando(false);
    }
  }
  
  
  async function excluirRegistro(item) {
    const id = item[configuracao.id];

    if (
      !window.confirm(
        `Deseja realmente excluir este ${configuracao.singular}?`
      )
    ) {
      return;
    }

    // Modo demonstração
    if (modoDemo) {
      setDados((dadosAtuais) => ({
        ...dadosAtuais,
        [pagina]: (dadosAtuais[pagina] || []).filter(
          (registro) => registro[configuracao.id] !== id
        ),
      }));

      avisar("Registro excluído com sucesso.");
      return;
    }

    // Integração com a API
    try {
      await api(`${configuracao.endpoint}/${id}`, {
        method: "DELETE",
      });

      await carregarCadastros();
      avisar("Registro excluído.");
    } catch (e) {
      avisar(e.message, "erro");
    }
  }



  const listaFiltrada = useMemo(() => {
    if (!configuracao) return [];
    return (dados[pagina] || []).filter((item) =>
      Object.values(item).some((valor) => String(valor ?? "").toLowerCase().includes(busca.toLowerCase()))
    );
  }, [dados, pagina, busca, configuracao]);

    async function carregarEquipe(codigo) {
    if (!codigo) {
      setEquipe({ atores: [], diretores: [], roteiristas: [] });
      return;
    }

    if (modoDemo) {
      const vinculos = vinculosDemo[String(codigo)] || {
        atores: [],
        diretores: [],
        roteiristas: [],
      };

      setEquipe({
        atores: dados.Atores.filter((p) => vinculos.atores.includes(Number(p.id))),
        diretores: dados.Diretores.filter((p) => vinculos.diretores.includes(Number(p.id))),
        roteiristas: dados.Roteiristas.filter((p) => vinculos.roteiristas.includes(Number(p.id))),
      });

      return;
    }

    try {
      const [atores, diretores, roteiristas] = await Promise.all([
        api(`/filmes/${codigo}/atores`),
        api(`/filmes/${codigo}/diretores`),
        api(`/filmes/${codigo}/roteiristas`),
      ]);

      setEquipe({
        atores: Array.isArray(atores) ? atores : [],
        diretores: Array.isArray(diretores) ? diretores : [],
        roteiristas: Array.isArray(roteiristas) ? roteiristas : [],
      });
    } catch (e) {
      avisar(e.message, "erro");
    }
  }

    async function vincularProfissional(evento) {
    evento.preventDefault();

    console.log("Modo demonstração:", modoDemo);
    console.log("Filme selecionado:", codigoFilme);
    console.log("Profissional selecionado:", idProfissional);
    console.log("Tipo de vínculo:", tipoVinculo);

    if (!codigoFilme || !idProfissional) {
      avisar("Selecione um filme e um profissional.", "erro");
      return;
    }

    if (modoDemo) {
      const id = Number(idProfissional);

      if (equipe[tipoVinculo].some((p) => Number(p.id) === id)) {
        avisar("Este profissional já está vinculado ao filme.", "erro");
        return;
      }

      const nomesListas = {
        atores: "Atores",
        diretores: "Diretores",
        roteiristas: "Roteiristas",
      };

      const profissional = dados[nomesListas[tipoVinculo]].find(
        (p) => Number(p.id) === id
      );

      if (!profissional) {
        avisar("Profissional não encontrado.", "erro");
        return;
      }

      setVinculosDemo((atual) => {
        const chave = String(codigoFilme);

        const vinculosFilme = atual[chave] || {
          atores: [],
          diretores: [],
          roteiristas: [],
        };

        return {
          ...atual,
          [chave]: {
            ...vinculosFilme,
            [tipoVinculo]: [...vinculosFilme[tipoVinculo], id],
          },
        };
      });

      setEquipe((atual) => ({
        ...atual,
        [tipoVinculo]: [...atual[tipoVinculo], profissional],
      }));

      setIdProfissional("");
      avisar("Profissional vinculado ao filme.");
      return;
    }

    try {
      await api(`/filmes/${codigoFilme}/${tipoVinculo}/${idProfissional}`, {
        method: "POST",
      });

      await carregarEquipe(codigoFilme);
      setIdProfissional("");
      avisar("Profissional vinculado ao filme.");
    } catch (e) {
      avisar(e.message, "erro");
    }
  }

    async function removerVinculo(tipo, id) {
    if (!window.confirm("Deseja remover este profissional do filme?")) {
      return;
    }

    if (modoDemo) {
      const chave = String(codigoFilme);

      setVinculosDemo((atual) => {
        const vinculosFilme = atual[chave] || {
          atores: [],
          diretores: [],
          roteiristas: [],
        };

        return {
          ...atual,
          [chave]: {
            ...vinculosFilme,
            [tipo]: vinculosFilme[tipo].filter(
              (idVinculado) => Number(idVinculado) !== Number(id)
            ),
          },
        };
      });

      setEquipe((atual) => ({
        ...atual,
        [tipo]: atual[tipo].filter(
          (pessoa) => Number(pessoa.id) !== Number(id)
        ),
      }));

      avisar("Vínculo removido.");
      return;
    }

    try {
      await api(`/filmes/${codigoFilme}/${tipo}/${id}`, {
        method: "DELETE",
      });

      await carregarEquipe(codigoFilme);
      avisar("Vínculo removido.");
    } catch (e) {
      avisar(e.message, "erro");
    }
  }

    async function carregarRelatorio(tipo) {
    const caminhos = {
      filmes: "/relatorios/filmes",
      profissionais: "/relatorios/profissionais",
      equipes: "/relatorios/elencos-equipes",
    };

    setRelatorio(null);
    avisar("");

    try {
      const resultado = await api(caminhos[tipo]);

      setRelatorio({
        tipo: tipo,
        dados: resultado,
      });
    } catch (e) {
      avisar(e.message, "erro");
    }
  }

  async function salvarPerfil(evento) {
    evento.preventDefault();
    if (!perfil) return;
    const campos = ["nome", "nacionalidade", "idade", "cpf", "cache", "papel", "tipoPersonagem", "salario", "estiloDirecao", "especialidade"];
    const payload = {};
    campos.forEach((campo) => {
      const valor = perfil[campo];
      // O CPF não vem na consulta do perfil; só é enviado se for preenchido.
      if (valor !== undefined && valor !== "") payload[campo] = valor;
    });
    if (payload.idade !== undefined) payload.idade = Number(payload.idade);
    ["cache", "salario"].forEach((campo) => {
      if (payload[campo] !== undefined) payload[campo] = Number(payload[campo]);
    });
    try {
      const resposta = await api("/minha-conta", { method: "PUT", body: JSON.stringify(payload) });
      setPerfil((anterior) => ({ ...anterior, ...resposta }));
      avisar("Seus dados foram atualizados.");
      await carregarPerfil();
    } catch (e) {
      avisar(e.message, "erro");
    }
  }


  function renderizarAutenticacao() {
    const tipoProfissional = cadastro.tipoUsuario;
    const mostrarProfissional = tipoProfissional !== "ADMINISTRADOR";

    const campoCadastro = (name, label, type = "text", obrigatorio = true) => (
      <label key={name}>
        {label}
        <input
          name={name}
          type={type}
          value={cadastro[name]}
          onChange={(evento) => setCadastro({ ...cadastro, [name]: evento.target.value })}
          required={obrigatorio}
          min={type === "number" ? "0" : undefined}
          step={name === "cache" || name === "salario" ? "0.01" : undefined}
        />
      </label>
    );

    return (
      <main className="tela-autenticacao" style={{ minHeight: "100vh", display: "grid", placeItems: "center", padding: "24px" }}>
        <section className="painel-formulario cartao-autenticacao" style={{ width: "min(100%, 620px)", margin: "0 auto" }}>
          <div style={{ textAlign: "center", marginBottom: "25px" }}>
            <div className="marca-icone" style={{ margin: "0 auto 12px", width: "58px", height: "48px" }}>
              <div className="claquete-faixa"><span /><span /><span /><span /></div>
              <div className="claquete-corpo">AS</div>
            </div>
            <span className="etiqueta">AUDIOVISUAL STUDIO</span>
            <h1 style={{ color: "#4b1518", margin: "9px 0" }}>
              {modoAcesso === "login" ? "Bem-vinda de volta" : "Crie sua conta"}
            </h1>
            <p style={{ color: "#81736e", margin: 0, fontSize: "13px" }}>
              {modoAcesso === "login"
                ? "Entre para gerenciar suas produções audiovisuais."
                : "Preencha os dados para criar seu acesso."}
            </p>
          </div>

          {erro && (
            <p role="alert" style={{ color: "#8b1717", background: "#fff1ef", padding: "11px", borderRadius: "7px" }}>
              {erro}
            </p>
          )}

          {sucesso && (
            <p role="status" style={{ color: "#286044", background: "#eff8f1", padding: "11px", borderRadius: "7px" }}>
              {sucesso}
            </p>
          )}

          {modoAcesso === "login" ? (
            <form onSubmit={fazerLogin}>
              <div className="campos-formulario">
                <label className="campo-completo">
                  E-mail
                  <input
                    type="email"
                    value={login.email}
                    onChange={(e) => setLogin({ ...login, email: e.target.value })}
                    required
                    autoComplete="username"
                  />
                </label>

                <label className="campo-completo">
                  Senha
                  <input
                    type="password"
                    value={login.senha}
                    onChange={(e) => setLogin({ ...login, senha: e.target.value })}
                    required
                    autoComplete="current-password"
                  />
                </label>

                <label className="campo-completo">
                  Entrar como
                  <select
                    className="campo-select"
                    value={login.entrarComo}
                    onChange={(e) => setLogin({ ...login, entrarComo: e.target.value })}
                  >
                    <option value="ADMINISTRADOR">Administrador</option>
                    <option value="PROFISSIONAL">Profissional</option>
                  </select>
                </label>
              </div>

              <div className="acoes-formulario">
                <button className="botao-principal" type="submit" disabled={carregando}>
                  {carregando ? "Entrando..." : "Entrar"}
                </button>
              </div>
            </form>
          ) : (
            <form onSubmit={fazerCadastro}>
              <div className="campos-formulario">
                <label className="campo-completo">
                  Tipo de conta
                  <select
                    className="campo-select"
                    value={cadastro.tipoUsuario}
                    onChange={(e) => setCadastro({ ...cadastro, tipoUsuario: e.target.value })}
                  >
                    <option value="ATOR">Ator</option>
                    <option value="DIRETOR">Diretor</option>
                    <option value="ROTEIRISTA">Roteirista</option>
                    <option value="ADMINISTRADOR">Administrador</option>
                  </select>
                </label>

                {campoCadastro("email", "E-mail", "email")}
                {campoCadastro("senha", "Senha (mínimo de 6 caracteres)", "password")}

                {mostrarProfissional && (
                  <>
                    {campoCadastro("nome", "Nome completo")}
                    {campoCadastro("nacionalidade", "Nacionalidade")}
                    {campoCadastro("idade", "Idade", "number")}
                    {campoCadastro("cpf", "CPF")}

                    {tipoProfissional === "ATOR" && (
                      <>
                        {campoCadastro("cache", "Cachê (R$)", "number")}
                        {campoCadastro("papel", "Papel")}
                        {campoCadastro("tipoPersonagem", "Tipo de personagem")}
                      </>
                    )}

                    {tipoProfissional === "DIRETOR" && (
                      <>
                        {campoCadastro("salario", "Salário (R$)", "number")}
                        {campoCadastro("estiloDirecao", "Estilo de direção")}
                      </>
                    )}

                    {tipoProfissional === "ROTEIRISTA" && (
                      <>
                        {campoCadastro("salario", "Salário (R$)", "number")}
                        {campoCadastro("especialidade", "Especialidade")}
                      </>
                    )}
                  </>
                )}
              </div>

              <div className="acoes-formulario">
                <button className="botao-principal" type="submit" disabled={carregando}>
                  {carregando ? "Cadastrando..." : "Criar conta"}
                </button>
              </div>
            </form>
          )}

          {modoAcesso === "login" && (
            <div style={{ marginTop: "18px", textAlign: "center" }}>
              <p style={{ color: "#81736e", fontSize: "12px", marginBottom: "10px" }}>
                Quer conhecer o sistema antes da integração?
              </p>
              <button
                type="button"
                className="botao-secundario"
                style={{ width: "100%", minHeight: "42px" }}
                onClick={entrarModoDemo}
              >
                Entrar em modo de demonstração
              </button>
            </div>
          )}

          <p style={{ textAlign: "center", color: "#81736e", fontSize: "13px", margin: "22px 0 0" }}>
            {modoAcesso === "login" ? "Ainda não tem uma conta? " : "Já tem uma conta? "}
            <button
              type="button"
              className="botao-secundario"
              style={{ minHeight: "auto", padding: "5px 9px" }}
              onClick={() => {
                setModoAcesso(modoAcesso === "login" ? "cadastro" : "login");
                avisar("");
              }}
            >
              {modoAcesso === "login" ? "Cadastre-se" : "Voltar ao login"}
            </button>
          </p>
        </section>
      </main>
    );
  }


  function renderizarInicio() {
    const cartoes = [
      ["Atores", dados.Atores.length, "◉"],
      ["Diretores", dados.Diretores.length, "✦"],
      ["Roteiristas", dados.Roteiristas.length, "✎"],
      ["Filmes", dados.Filmes.length, "▣"],
    ];
    return (
      <>
        <section className="boas-vindas">
          <span className="etiqueta">PAINEL DE CONTROLE</span>
          <h2>{administrador ? "Organize suas produções." : "Bem-vindo ao seu espaço."}</h2>
          <p>{administrador ? "Gerencie atores, diretores, roteiristas, filmes e suas equipes em um só lugar." : "Consulte e mantenha seus dados profissionais atualizados."}</p>
        </section>
        {administrador ? <>
          <h3 className="titulo-secao">Resumo dos cadastros</h3>
          <section className="cartoes">
            {cartoes.map(([nome, quantidade, icone]) => (
              <button className="cartao cartao-botao" key={nome} onClick={() => abrirPagina(nome)}>
                <span className="cartao-icone">{icone}</span>
                <span className="cartao-titulo">{nome}</span>
                <strong className="cartao-numero">{quantidade}</strong>
                <span className="cartao-link">Ver cadastros →</span>
              </button>
            ))}
          </section>
        </> : (
        <>
          <section className="aviso">
            <strong>Área do profissional</strong>
            <p>Acesse “Minha conta” no menu para consultar ou atualizar seus dados.</p>
          </section>

          {perfil?.tipoUsuario === "ATOR" && (
            <section className="painel-tabela" style={{ marginTop: "24px" }}>
              <h3 style={{ color: "#4b1518", marginTop: 0 }}>
                Meus filmes
              </h3>

              {filmesDoProfissional.length > 0 ? (
                <div className="tabela-rolagem">
                  <table className="tabela">
                    <thead>
                      <tr>
                        <th>Título</th>
                        <th>Gênero</th>
                        <th>Duração</th>
                      </tr>
                    </thead>

                    <tbody>
                      {filmesDoProfissional.map((filme) => (
                        <tr key={filme.codigo}>
                          <td>{filme.titulo}</td>
                          <td>{filme.genero}</td>
                          <td>{filme.duracao} min</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <p>Nenhum filme atribuído a você no momento.</p>
              )}
            </section>
          )}
        </>
      )}
      </>
    );
  }

  function renderizarCadastro() {
    return (
      <>
        <section className="barra-acoes">
          <div><h2>Cadastro de {configuracao.plural}</h2><p>Consulte e gerencie os registros cadastrados.</p></div>
          <button className="botao-principal" onClick={novoRegistro}>+ Novo {configuracao.singular}</button>
        </section>
        {formularioAberto && <section className="painel-formulario">
          <div className="formulario-cabecalho">
            <div><h2>{registroEditado !== null ? `Editar ${configuracao.singular}` : `Novo ${configuracao.singular}`}</h2><p>Preencha os campos abaixo para salvar o cadastro.</p></div>
            <button type="button" className="botao-secundario" onClick={() => setFormularioAberto(false)}>Cancelar</button>
          </div>
          <form onSubmit={salvarRegistro}>
            <div className="campos-formulario">
              {configuracao.campos.map((campo) => (
                <label key={campo.name}>{campo.label}{campo.required ? " *" : ""}
                  <input
                    name={campo.name}
                    type={campo.type || "text"}
                    placeholder={campo.placeholder || ""}
                    min={campo.type === "number" ? "0" : undefined}
                    step={campo.step}
                    value={formulario[campo.name] ?? ""}
                    onChange={(e) => setFormulario({ ...formulario, [campo.name]: e.target.value })}
                    required={campo.required}
                  />
                </label>
              ))}
            </div>
            <div className="acoes-formulario"><button className="botao-principal" type="submit" disabled={carregando}>{carregando ? "Salvando..." : "Salvar cadastro"}</button></div>
          </form>
        </section>}
        <section className="painel-tabela">
          <div className="pesquisa"><input value={busca} onChange={(e) => setBusca(e.target.value)} placeholder={`Pesquisar ${configuracao.plural}...`} /></div>
          <div className="tabela-rolagem"><table className="tabela">
            <thead><tr>{configuracao.titulos.map((titulo) => <th key={titulo}>{titulo}</th>)}<th>Ações</th></tr></thead>
            <tbody>
              {listaFiltrada.map((item) => <tr key={item[configuracao.id]}>
                {configuracao.colunas.map((coluna) => <td key={coluna}>{formatarValor(coluna, item[coluna] ?? (coluna === "codigo" ? item.codigo : undefined))}</td>)}
                <td><button className="botao-secundario" onClick={() => editarRegistro(item)}>Editar</button> <button className="botao-excluir" onClick={() => excluirRegistro(item)}>Excluir</button></td>
              </tr>)}
              {listaFiltrada.length === 0 && <tr><td colSpan={configuracao.titulos.length + 1} className="tabela-vazia">{busca ? "Nenhum resultado encontrado." : `Nenhum ${configuracao.singular} cadastrado ainda.`}</td></tr>}
            </tbody>
          </table></div>
        </section>
      </>
    );
  }

  function renderizarAtribuicoes() {
    const opcoes = dados[tipoVinculo === "atores" ? "Atores" : tipoVinculo === "diretores" ? "Diretores" : "Roteiristas"] || [];
    const nomes = { atores: "ator", diretores: "diretor", roteiristas: "roteirista" };
    const vinculos = [
      { tipo: "atores", titulo: "Atores", lista: equipe.atores },
      { tipo: "diretores", titulo: "Diretores", lista: equipe.diretores },
      { tipo: "roteiristas", titulo: "Roteiristas", lista: equipe.roteiristas },
    ];
    return <>
      <section className="barra-acoes"><div><h2>Atribuições e elenco</h2><p>Associe atores, diretores e roteiristas aos filmes cadastrados.</p></div></section>
      <section className="painel-formulario">
        <div className="formulario-cabecalho"><div><h2>Vincular profissional ao filme</h2><p>Escolha a produção e o profissional que fará parte da equipe.</p></div></div>
        <form onSubmit={vincularProfissional}>
          <div className="campos-formulario">
            <label>Filme *<select className="campo-select" value={codigoFilme} onChange={(e) => { setCodigoFilme(e.target.value); setIdProfissional(""); carregarEquipe(e.target.value); }} required>
              <option value="">Selecione um filme</option>{dados.Filmes.map((filme) => <option key={filme.codigo} value={filme.codigo}>[{filme.codigo}] {filme.titulo}</option>)}
            </select></label>
            <label>Tipo de profissional *<select className="campo-select" value={tipoVinculo} onChange={(e) => { setTipoVinculo(e.target.value); setIdProfissional(""); }}>
              <option value="atores">Ator (elenco)</option><option value="diretores">Diretor</option><option value="roteiristas">Roteirista</option>
            </select></label>
            <label className="campo-completo">Profissional *<select className="campo-select" value={idProfissional} onChange={(e) => setIdProfissional(e.target.value)} required>
              <option value="">Selecione um profissional</option>{opcoes.map((item) => <option key={item.id} value={item.id}>{item.nome} — CPF: {item.cpf}</option>)}
            </select></label>
          </div>
          <div className="acoes-formulario"><button className="botao-principal" type="submit">+ Vincular ao filme</button></div>
        </form>
      </section>
      {codigoFilme && <section className="painel-tabela">
        <h3 style={{ marginTop: 0, color: "#4b1518" }}>Elenco e equipe do filme selecionado</h3>
        {vinculos.map((grupo) => <div key={grupo.tipo} style={{ marginTop: "20px" }}>
          <h4>{grupo.titulo}</h4>
          <div className="tabela-rolagem"><table className="tabela"><thead><tr><th>Nome</th><th>CPF</th><th>Ação</th></tr></thead><tbody>
            {grupo.lista.map((pessoa) => <tr key={pessoa.id}><td>{pessoa.nome}</td><td>{pessoa.cpf || "—"}</td><td><button className="botao-excluir" onClick={() => removerVinculo(grupo.tipo, pessoa.id)}>Remover</button></td></tr>)}
            {grupo.lista.length === 0 && <tr><td colSpan="3" className="tabela-vazia">Nenhum {nomes[grupo.tipo]} vinculado.</td></tr>}
          </tbody></table></div>
        </div>)}
      </section>}
    </>;
  }

  

  function renderizarRelatorios() {
    
  function abrirRelatorio(tipo) {
    if (!modoDemo) {
      console.log("Relatório solicitado:", tipo);
      carregarRelatorio(tipo);
      return;
    }

    if (tipo === "filmes") {
      setRelatorio({
        tipo,
        dados: {
          totalFilmes: dados.Filmes.length,
          filmes: dados.Filmes,
        },
      });
    }

    if (tipo === "profissionais") {
      const profissionais = [
        ...dados.Atores.map((p) => ({ ...p, tipo: "Ator" })),
        ...dados.Diretores.map((p) => ({ ...p, tipo: "Diretor" })),
        ...dados.Roteiristas.map((p) => ({ ...p, tipo: "Roteirista" })),
      ];

      setRelatorio({
        tipo,
        dados: {
          totalProfissionais: profissionais.length,
          profissionais,
        },
      });
    }

    if (tipo === "equipes") {
      const producoes = dados.Filmes.map((filme) => {
        const vinculos = vinculosDemo[String(filme.codigo)] || {
          atores: [],
          diretores: [],
          roteiristas: [],
        };

        return {
          ...filme,
          elenco: dados.Atores.filter((p) =>
            vinculos.atores.includes(Number(p.id))
          ),
          diretores: dados.Diretores.filter((p) =>
            vinculos.diretores.includes(Number(p.id))
          ),
          roteiristas: dados.Roteiristas.filter((p) =>
            vinculos.roteiristas.includes(Number(p.id))
          ),
        };
      });

      setRelatorio({
        tipo,
        dados: {
          producoes,
        },
      });
    }

    setErro("");
}


    return (
      <>
        <section className="barra-acoes">
          <div>
            <h2>Relatórios</h2>
            <p>Consulte os relatórios disponibilizados pelo sistema.</p>
          </div>
        </section>

        <section className="cartoes">
          <button className="cartao cartao-botao" onClick={() => abrirRelatorio("filmes")}>
            <span className="cartao-icone">▣</span>
            <span className="cartao-titulo">Relatório de filmes</span>
            <strong className="cartao-numero">Filmes</strong>
            <span className="cartao-link">Visualizar relatório →</span>
          </button>

          <button className="cartao cartao-botao" onClick={() => abrirRelatorio("profissionais")}>
            <span className="cartao-icone">◉</span>
            <span className="cartao-titulo">Relatório de profissionais</span>
            <strong className="cartao-numero">Equipe</strong>
            <span className="cartao-link">Visualizar relatório →</span>
          </button>

          <button className="cartao cartao-botao" onClick={() => abrirRelatorio("equipes")}>
            <span className="cartao-icone">✦</span>
            <span className="cartao-titulo">Elencos e equipes</span>
            <strong className="cartao-numero">Produções</strong>
            <span className="cartao-link">Visualizar relatório →</span>
          </button>
        </section>

        {relatorio && (
          <section className="painel-tabela" style={{ marginTop: "24px" }}>
            <h3 style={{ color: "#4b1518", marginTop: 0 }}>
              {relatorio.tipo === "filmes"
                ? "Relatório de filmes"
                : relatorio.tipo === "profissionais"
                  ? "Relatório de profissionais"
                  : "Relatório de elencos e equipes"}
            </h3>

            {relatorio.tipo === "filmes" && (
              <>
                <p>Total de filmes: <strong>{relatorio.dados.totalFilmes}</strong></p>
                <div className="tabela-rolagem">
                  <table className="tabela">
                    <thead>
                      <tr>
                        <th>Código</th>
                        <th>Título</th>
                        <th>Gênero</th>
                        <th>Duração</th>
                      </tr>
                    </thead>
                    <tbody>
                      {relatorio.dados.filmes?.map((f) => (
                        <tr key={f.codigo}>
                          <td>{f.codigo}</td>
                          <td>{f.titulo}</td>
                          <td>{f.genero}</td>
                          <td>{f.duracao} min</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </>
            )}

            
            {relatorio.tipo === "profissionais" && (
              <>
                <p>
                  Total de profissionais:{" "}
                  <strong>{relatorio.dados.totalProfissionais}</strong>
                </p>

                <div className="tabela-rolagem">
                  <table className="tabela">
                    <thead>
                      <tr>
                        <th>Tipo</th>
                        <th>Nome</th>
                        <th>Nacionalidade</th>
                        <th>Idade</th>
                        <th>CPF</th>
                      </tr>
                    </thead>

                    <tbody>
                      {[
                        ...(relatorio.dados.atores || []),
                        ...(relatorio.dados.diretores || []),
                        ...(relatorio.dados.roteiristas || [])
                      ].map((p) => (
                        <tr key={`${p.profissao}-${p.id}`}>
                          <td>{p.profissao}</td>
                          <td>{p.nome}</td>
                          <td>{p.nacionalidade || "—"}</td>
                          <td>{p.idade ?? "—"}</td>
                          <td>{p.cpf || "—"}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </>
            )}


            {relatorio.tipo === "equipes" && (
              <>
                <p>Total de produções: <strong>{relatorio.dados.producoes?.length || 0}</strong></p>
                <div className="tabela-rolagem">
                  <table className="tabela">
                    <thead>
                      <tr>
                        <th>Filme</th>
                        <th>Elenco</th>
                        <th>Direção</th>
                        <th>Roteiro</th>
                      </tr>
                    </thead>
                    <tbody>
                      {relatorio.dados.producoes?.map((f) => (
                        <tr key={f.codigo}>
                          <td>{f.titulo}</td>
                          <td>{(f.elenco || []).map((p) => p.nome).join(", ") || "—"}</td>
                          <td>{(f.diretores || []).map((p) => p.nome).join(", ") || "—"}</td>
                          <td>{(f.roteiristas || []).map((p) => p.nome).join(", ") || "—"}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </>
            )}
          </section>
        )}
      </>
    );
  }



  function renderizarPerfil() {
    if (!perfil) return <section className="painel-tabela">Carregando dados da conta...</section>;
    const campos = [
      { name: "nome", label: "Nome completo", type: "text" },
      { name: "nacionalidade", label: "Nacionalidade", type: "text" },
      { name: "idade", label: "Idade", type: "number" },
      { name: "cpf", label: "CPF (opcional para atualização)", type: "text" },
      ...(perfil.tipoUsuario === "ATOR" ? [
        { name: "cache", label: "Cachê (R$)", type: "number" },
        { name: "papel", label: "Papel", type: "text" },
        { name: "tipoPersonagem", label: "Tipo de personagem", type: "text" },
      ] : []),
      ...(perfil.tipoUsuario === "DIRETOR" ? [
        { name: "salario", label: "Salário (R$)", type: "number" },
        { name: "estiloDirecao", label: "Estilo de direção", type: "text" },
      ] : []),
      ...(perfil.tipoUsuario === "ROTEIRISTA" ? [
        { name: "salario", label: "Salário (R$)", type: "number" },
        { name: "especialidade", label: "Especialidade", type: "text" },
      ] : []),
    ];
    return <>
      <section className="barra-acoes"><div><h2>Minha conta</h2><p>Consulte e atualize seus dados profissionais.</p></div></section>
      <section className="painel-formulario"><form onSubmit={salvarPerfil}>
        <div className="campos-formulario">
          <label>E-mail<input value={perfil.email || ""} disabled /></label>
          <label>Tipo de conta<input value={perfil.tipoUsuario || ""} disabled /></label>
          {campos.map((campo) => <label key={campo.name}>{campo.label}<input type={campo.type} min={campo.type === "number" ? "0" : undefined} step={["cache", "salario"].includes(campo.name) ? "0.01" : undefined} value={perfil[campo.name] ?? ""} onChange={(e) => setPerfil({ ...perfil, [campo.name]: e.target.value })} /></label>)}
        </div>
        <div className="acoes-formulario"><button type="submit" className="botao-principal">Salvar alterações</button></div>
      </form></section>
    </>;
  }

  if (!usuario) return renderizarAutenticacao();

  return <div className="aplicacao">
    <MenuLateral paginaAtual={pagina} aoMudarPagina={abrirPagina} paginas={paginas} />
    <main className="conteudo">
      <header className="cabecalho">
        <div><span className="caminho">AUDIOVISUAL STUDIO /</span><h1>{pagina}</h1></div>
        <div className="perfil">
          <div className="avatar">{administrador ? "AD" : "PR"}</div>
          <div><strong>{usuario.email}</strong><span>{usuario.tipoUsuario}</span></div>
          <button className="botao-secundario" onClick={sair}>Sair</button>
        </div>
      </header>

      {erro && <p role="alert" style={{ color: "#8b1717", background: "#fff1ef", padding: "11px 14px", borderRadius: "7px", marginBottom: "18px" }}>{erro}</p>}
      {sucesso && <p role="status" style={{ color: "#286044", background: "#eff8f1", padding: "11px 14px", borderRadius: "7px", marginBottom: "18px" }}>{sucesso}</p>}
      {carregando && <p style={{ color: "#81736e", fontSize: "12px" }}>Carregando...</p>}

      {pagina === "Visão geral" && renderizarInicio()}
      {configuracao && administrador && renderizarCadastro()}
      {pagina === "Atribuições" && administrador && renderizarAtribuicoes()}
      {pagina === "Relatórios" && administrador && renderizarRelatorios()}
      {pagina === "Minha conta" && !administrador && renderizarPerfil()}
    </main>
  </div>;
}

export default App;
