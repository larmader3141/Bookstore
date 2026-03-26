1) upload a file
$apiBaseUrl = "https://your-api-id.execute-api.your-region.amazonaws.com/Prod"
$response = Invoke-RestMethod "$apiBaseUrl/books/upload-url"

curl.exe -X PUT `
  -T ".\sample-upload.pdf" `
  "$($response.uploadUrl)"

2) upload the metadata
Invoke-RestMethod `
  -Uri "$apiBaseUrl/books" `
  -Method Post `
  -Body (@{
      title="Sample Book"
      author="Sample Author"
      fileKey="books/sample-file-id.pdf"
  } | ConvertTo-Json) `
  -ContentType "application/json"

3) get the book
$bookId = "sample-book-id"
$response = Invoke-RestMethod "$apiBaseUrl/books/$bookId"

4) get the download url
$response = Invoke-RestMethod "$apiBaseUrl/books/$bookId"

5) download the book
Invoke-WebRequest `
  -Uri $response.downloadUrl `
  -OutFile ".\downloaded.pdf"




