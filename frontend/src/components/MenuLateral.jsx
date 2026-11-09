import React from "react";

function MenuLateral({ paginaAtual, aoMudarPagina, paginas }) {
  return (
    <aside className="menu-lateral">
      <div className="marca">
        <div className="marca-icone">
          <div className="claquete-faixa">
            <span />
            <span />
            <span />
            <span />
          </div>
          <div className="claquete-corpo">AS</div>
        </div>

        <div>
          <strong>Audiovisual</strong>
          <span>STUDIO</span>
        </div>
      </div>

      <p className="menu-legenda">MENU PRINCIPAL</p>

      {paginas.map((nomePagina) => (
        <button
          key={nomePagina}
          className={`item-menu ${paginaAtual === nomePagina ? "ativo" : ""}`}
          onClick={() => aoMudarPagina(nomePagina)}
        >
          {nomePagina === "Visão geral" && "⌂ "}
          {nomePagina === "Atores" && "◉ "}
          {nomePagina === "Diretores" && "✦ "}
          {nomePagina === "Roteiristas" && "✎ "}
          {nomePagina === "Filmes" && "▣ "}
          {nomePagina === "Atribuições" && "⇄ "}
          {nomePagina === "Relatórios" && "▤ "}
          {nomePagina}
        </button>
      ))}

      <div className="menu-rodape">Audiovisual Studio · 2026</div>
    </aside>
  );
}

export default MenuLateral;