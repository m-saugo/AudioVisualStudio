import React from "react";

function Cabecalho({ pagina }) {
  return (
    <header className="cabecalho">
      <div>
        <span className="caminho">AUDIOVISUAL STUDIO /</span>
        <h1>{pagina}</h1>
      </div>

      <div className="perfil">
        <div className="avatar">AS</div>
        <div>
          <strong>Produção</strong>
          <span>Administrador</span>
        </div>
      </div>
    </header>
  );
}

export default Cabecalho;