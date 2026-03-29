import { useState } from "react";
import { useEffect } from "react";


const API = "https://9isvgba449.execute-api.us-west-1.amazonaws.com/Prod";

export default function App() {

  const [title, setTitle] = useState("");
  const [author, setAuthor] = useState("");
  const [file, setFile] = useState(null);
  const [result, setResult] = useState(null);

  const [books, setBooks] = useState([]);

    const fetchBooks = async () => {
      const res = await fetch(`${API}/books`);
      const data = await res.json();

      setBooks(data);
    };

    const downloadBook = async (bookId) => {
      const res = await fetch(`${API}/books/${bookId}`);
      const data = await res.json();

      window.open(data.downloadUrl, "_blank");
    };

  const uploadBook = async () => {
    // 1. get upload URL
    const uploadRes = await fetch(`${API}/books/upload-url`);
    const uploadData = await uploadRes.json();

    // 2. upload file to S3
    await fetch(uploadData.uploadUrl, {
      method: "PUT",
      body: file,
    });

    // 3. save metadata
    const saveRes = await fetch(`${API}/books`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        title: title,
        author: author,
        fileKey: uploadData.fileKey,
      }),
    });

    const data = await saveRes.json();
    setResult(data);
  };

    useEffect(() => {
      fetchBooks();
    }, []);

  return (
      <div>
      <h1>Bookstore</h1>
      <h2>Book List</h2>

      <button onClick={fetchBooks}>Refresh</button>

      <ul>
        {books.map((book) => (
          <li key={book.bookId}>
            <strong>{book.title}</strong> by {book.author}
            <br />

            <button onClick={() => downloadBook(book.bookId)}>
              Download
            </button>

            <hr />
          </li>
        ))}
      </ul>

    <hr />
      <input placeholder="Title" value={title} onChange={(e) => setTitle(e.target.value)} />
      <input placeholder="Author" value={author} onChange={(e) => setAuthor(e.target.value)} />
      <input type="file" onChange={(e) => setFile(e.target.files[0])} />

      <button onClick={uploadBook}>Upload</button>

      <pre>{JSON.stringify(result, null, 2)}</pre>
    </div>
  );
}