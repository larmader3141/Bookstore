import { useState } from "react";
import { useEffect } from "react";
import "./App.css";

const API = "https://9isvgba449.execute-api.us-west-1.amazonaws.com/Prod";

export default function App() {

  const [title, setTitle] = useState("");
  const [author, setAuthor] = useState("");
  const [file, setFile] = useState(null);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const [books, setBooks] = useState([]);

  const fetchBooks = async () => {
      try {
        setLoading(true);
        const res = await fetch(`${API}/books`);
        const data = await res.json();
        setBooks(data);
      } finally {
        setLoading(false);
      }
  };

    const downloadBook = async (bookId) => {
      const res = await fetch(`${API}/books/${bookId}`);
      const data = await res.json();

      window.open(data.downloadUrl, "_blank");
    };

    const deleteBook = async (bookId) => {
      if (!confirm("Are you sure?")) return;

      try {
        setLoading(true);

        await fetch(`${API}/books/${bookId}`, {
          method: "DELETE",
        });

        fetchBooks();

      } finally {
        setLoading(false);
      }
    };

    const uploadBook = async () => {
      try {
        setLoading(true);

        const uploadRes = await fetch(`${API}/books/upload-url`);
        const uploadData = await uploadRes.json();

        await fetch(uploadData.uploadUrl, {
          method: "PUT",
          body: file,
        });

        const saveRes = await fetch(`${API}/books`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            title,
            author,
            fileKey: uploadData.fileKey,
          }),
        });

        const data = await saveRes.json();
        setResult(data);

      } finally {
        setLoading(false);
      }
          // refresh list
          fetchBooks();

      };

    useEffect(() => {
      fetchBooks();
    }, []);

  return (
      <div style={{ cursor: loading ? "wait" : "default" }}>
      <h1>Bookstore</h1>
      <h2>Add a book</h2>
        <hr />
          <input placeholder="Title" value={title} onChange={(e) => setTitle(e.target.value)} />
          <input placeholder="Author" value={author} onChange={(e) => setAuthor(e.target.value)} />
          <input type="file" onChange={(e) => setFile(e.target.files[0])} />

          <button onClick={uploadBook} disabled={loading}>Upload</button>

{/*           <pre>{JSON.stringify(result, null, 2)}</pre> */}

    <div>
           <br />
    </div>
          <h2>Book List</h2>
    <div>
           <br />
    </div>

      {loading && <p>⏳ Processing...</p>}

      <button onClick={fetchBooks}>Refresh</button>

    <ul class="no-bullets">
        {books.map((book) => (
          <li key={book.bookId}>
            <strong>{book.title}</strong> by {book.author}
            <br />

            <button onClick={() => downloadBook(book.bookId)}>
              Download
            </button>

            <button onClick={() => deleteBook(book.bookId)}>
              Delete
            </button>
            <hr />
          </li>
        ))}
      </ul>

    </div>
  );
}