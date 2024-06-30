import "./Page404.css";

export function Page404(): JSX.Element {
  return (
    <div className="Page404">
      <h1>Oops! We couldn't find the page you're looking for.</h1>
      <p>
        It seems the page you requested either never existed, has been moved, or
        is temporarily unavailable. Don't worry, you can try the following:
      </p>
      <ul>
        <li>
          <a href="/">Go back to the homepage</a>
        </li>
      </ul>
    </div>
  );
}
