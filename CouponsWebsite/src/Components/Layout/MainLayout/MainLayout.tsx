import { Header } from "../Header/Header";
import "./MainLayout.css";
import { Footer } from "../Footer/Footer";
import { MainRoute } from "../../Pages/Route/MainRoute/MainRoute";
import { Menu } from "../Menu/Menu";
import { useState } from "react";

export function MainLayout(): JSX.Element {
  const [render, setRender] = useState(false);
  return (
    <div className="MainLayout">
      <header>
        <Header setRender={setRender} />
      </header>
      <div style={{ padding: 10 }}>
        <Menu render={render} setRender={setRender} />
      </div>
      <main>
        <MainRoute setRender={setRender} />
      </main>
      <footer>
        <Footer />
      </footer>
    </div>
  );
}
