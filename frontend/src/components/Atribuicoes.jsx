// Desenvolvido por: Maria E. F. Lunelli
import { useState } from "react";

function Atribuicoes({ filmes, atores, diretores, roteiristas }) {
  const [filmeSelecionadoId, setFilmeSelecionadoId] = useState("");
  const [tipoProfissional, setTipoProfissional] = useState("ator");
  const [profissionalId, setProfissionalId] = useState("");

  // Estado para armazenar os vínculos entre profissionais e filmes
  const [atribuicoes, setAtribuicoes] = useState([]);

  function vincularProfissional(evento) {
    evento.preventDefault();

    if (!filmeSelecionadoId || !profissionalId) {
      alert("Selecione um filme e um profissional!");
      return;
    }

    // Verifica se já não está vinculado
    const jaExiste = atribuicoes.some(
      (item) =>
        item.filmeId === String(filmeSelecionadoId) &&
        item.profissionalId === String(profissionalId) &&
        item.tipo === tipoProfissional
    );

    if (jaExiste) {
      alert("Este profissional já está vinculado a este filme!");
      return;
    }

    const novaAtribuicao = {
      id: Date.now(),
      filmeId: String(filmeSelecionadoId),
      profissionalId: String(profissionalId),
      tipo: tipoProfissional,
    };

    setAtribuicoes([...atribuicoes, novaAtribuicao]);
    setProfissionalId("");
  }

  function removerAtribuicao(id) {
    if (window.confirm("Deseja remover este profissional do filme?")) {
      setAtribuicoes(atribuicoes.filter((item) => item.id !== id));
    }
  }

  // Lista de profissionais do tipo selecionado no select
  const listaProfissionaisOpcoes =
    tipoProfissional === "ator"
      ? atores
      : tipoProfissional === "diretor"
      ? diretores
      : roteiristas;

  const filmeAtual = filmes.find((f) => String(f.id) === String(filmeSelecionadoId));

  const atribuicoesDoFilme = atribuicoes.filter(
    (item) => item.filmeId === String(filmeSelecionadoId)
  );

  return (
    <>
      <section className="barra-acoes">
        <div>
          <h2>Atribuições e Elenco</h2>
          <p>Associe atores, diretores e roteiristas aos filmes da produção.</p>
        </div>
      </section>

      <section className="painel-formulario">
        <div className="formulario-cabecalho">
          <div>
            <h2>Vincular Profissional ao Filme</h2>
            <p>Selecione a obra e o integrante da equipe para formar o elenco.</p>
          </div>
        </div>

        <form onSubmit={vincularProfissional}>
          <div className="campos-formulario">
            <label>
              Selecione o Filme *
              <select
                className="campo-select"
                value={filmeSelecionadoId}
                onChange={(e) => setFilmeSelecionadoId(e.target.value)}
                required
              >
                <option value="">-- Selecione um filme --</option>
                {filmes.map((filme) => (
                  <option key={filme.id} value={filme.id}>
                    [{filme.codigo}] {filme.titulo}
                  </option>
                ))}
              </select>
            </label>

            <label>
              Tipo de Profissional *
              <select
                className="campo-select"
                value={tipoProfissional}
                onChange={(e) => {
                  setTipoProfissional(e.target.value);
                  setProfissionalId("");
                }}
              >
                <option value="ator">Ator (Elenco)</option>
                <option value="diretor">Diretor (Equipe)</option>
                <option value="roteirista">Roteirista (Equipe)</option>
              </select>
            </label>

            <label className="campo-completo">
              Selecione o Profissional *
              <select
                className="campo-select"
                value={profissionalId}
                onChange={(e) => setProfissionalId(e.target.value)}
                required
              >
                <option value="">-- Selecione o profissional --</option>
                {listaProfissionaisOpcoes.map((prof) => (
                  <option key={prof.id} value={prof.id}>
                    {prof.nome} (CPF: {prof.cpf})
                  </option>
                ))}
              </select>
            </label>
          </div>

          <div className="acoes-formulario">
            <button type="submit" className="botao-principal">
              + Vincular ao Filme
            </button>
          </div>
        </form>
      </section>

      {/* Tabela de exibição do Elenco e Equipe do Filme selecionado */}
      <section className="painel-tabela">
        <h3>
          {filmeAtual
            ? `Equipe e Elenco de: "${filmeAtual.titulo}"`
            : "Selecione um filme acima para ver a equipe"}
        </h3>

        {filmeSelecionadoId && (
          <div className="tabela-rolagem" style={{ marginTop: "15px" }}>
            <table className="tabela">
              <thead>
                <tr>
                  <th>Função</th>
                  <th>Nome do Profissional</th>
                  <th>CPF</th>
                  <th>Detalhe</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {atribuicoesDoFilme.map((item) => {
                  let dadosProf = null;
                  let detalhe = "";

                  if (item.tipo === "ator") {
                    dadosProf = atores.find((a) => String(a.id) === item.profissionalId);
                    detalhe = dadosProf ? `Papel: ${dadosProf.papel}` : "";
                  } else if (item.tipo === "diretor") {
                    dadosProf = diretores.find((d) => String(d.id) === item.profissionalId);
                    detalhe = dadosProf ? `Estilo: ${dadosProf.estiloDirecao}` : "";
                  } else {
                    dadosProf = roteiristas.find((r) => String(r.id) === item.profissionalId);
                    detalhe = dadosProf ? `Especialidade: ${dadosProf.especialidade}` : "";
                  }

                  if (!dadosProf) return null;

                  return (
                    <tr key={item.id}>
                      <td>
                        <strong>
                          {item.tipo === "ator"
                            ? "Ator"
                            : item.tipo === "diretor"
                            ? "Diretor"
                            : "Roteirista"}
                        </strong>
                      </td>
                      <td>{dadosProf.nome}</td>
                      <td>{dadosProf.cpf}</td>
                      <td>{detalhe}</td>
                      <td>
                        <button
                          className="botao-excluir"
                          onClick={() => removerAtribuicao(item.id)}
                        >
                          Remover
                        </button>
                      </td>
                    </tr>
                  );
                })}

                {atribuicoesDoFilme.length === 0 && (
                  <tr>
                    <td colSpan="5" className="tabela-vazia">
                      Nenhum profissional vinculado a este filme ainda.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </>
  );
}

export default Atribuicoes;