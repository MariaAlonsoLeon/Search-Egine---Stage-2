import React, { useState } from 'react';

const Search = ({ searchType }) => {
    const [word, setWord] = useState('');
    const [author, setAuthor] = useState('');
    const [date, setDate] = useState('');
    const [language, setLanguage] = useState('');
    const [contextSize, setContextSize] = useState(10);
    const [documentFolderPath, setDocumentFolderPath] = useState('');
    const [bookName, setBookName] = useState('');
    const [dataType, setDataType] = useState('JSON');
    const [results, setResults] = useState(null);

    const handleSubmit = (e) => {
        e.preventDefault();
        let url = '';

        const BASE_URL = 'http://localhost:8080';

        switch (searchType) {
            case 'searchSingleWordII':
                url = `${BASE_URL}/search/${dataType}/single-word/${word}`;
                break;
            case 'searchWithContextII':
                url = `${BASE_URL}/search/${dataType}/with-context/${word}/${contextSize}/${documentFolderPath}`;
                break;
            case 'searchByAuthorMD':
                url = `${BASE_URL}/search/${dataType}/author/${author}`;
                break;
            case 'searchByDateMD':
                url = `${BASE_URL}/search/${dataType}/date/${date}`;
                break;
            case 'searchByLanguageMD':
                url = `${BASE_URL}/search/${dataType}/language/${language}`;
                break;
            case 'fetchMD':
                url = `${BASE_URL}/search/${dataType}/book/${bookName}`;
                break;
            default:
                break;
        }

        fetch(url)
            .then((response) => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then((data) => setResults(data))
            .catch((error) => console.error('Error fetching data:', error));
    };

    return (
        <div className="flex-grow p-4">
            <form onSubmit={handleSubmit} className="mb-4">
                <div className="flex flex-col mb-4">
                    <span className="font-semibold mb-2">Data Format:</span>
                    <label className="inline-flex items-center mb-1">
                        <input
                            type="radio"
                            name="format"
                            value="JSON"
                            checked={dataType === 'JSON'}
                            onChange={(e) => setDataType(e.target.value)}
                            className="mr-2"
                        />
                        <span className="ml-2">JSON</span>
                    </label>
                    <label className="inline-flex items-center mb-1">
                        <input
                            type="radio"
                            name="format"
                            value="BINARY"
                            checked={dataType === 'BINARY'}
                            onChange={(e) => setDataType(e.target.value)}
                            className="mr-2"
                        />
                        <span className="ml-2">BINARY</span>
                    </label>
                    <label className="inline-flex items-center mb-1">
                        <input
                            type="radio"
                            name="format"
                            value="NEO4J"
                            checked={dataType === 'NEO4J'}
                            onChange={(e) => setDataType(e.target.value)}
                            className="mr-2"
                        />
                        <span className="ml-2">NEO4J</span>
                    </label>
                    <label className="inline-flex items-center">
                        <input
                            type="radio"
                            name="format"
                            value="MONGODB"
                            checked={dataType === 'MONGODB'}
                            onChange={(e) => setDataType(e.target.value)}
                            className="mr-2"
                        />
                        <span className="ml-2">MONGODB</span>
                    </label>
                </div>

                {searchType === 'searchSingleWordII' && (
                    <div className="flex flex-col mb-2">
                        <label htmlFor="word" className="font-semibold">Word:</label>
                        <input
                            type="text"
                            id="word"
                            value={word}
                            onChange={(e) => setWord(e.target.value)}
                            className="border p-2"
                        />
                    </div>
                )}

                {searchType === 'searchWithContextII' && (
                    <>
                        <div className="flex flex-col mb-2">
                            <label htmlFor="word" className="font-semibold">Word:</label>
                            <input
                                type="text"
                                id="word"
                                value={word}
                                onChange={(e) => setWord(e.target.value)}
                                className="border p-2"
                            />
                        </div>
                        <div className="flex flex-col mb-2">
                            <label htmlFor="contextSize" className="font-semibold">Context Size:</label>
                            <input
                                type="number"
                                id="contextSize"
                                value={contextSize}
                                onChange={(e) => setContextSize(e.target.value)}
                                className="border p-2"
                            />
                        </div>
                        <div className="flex flex-col mb-2">
                            <label htmlFor="documentFolderPath" className="font-semibold">Document Folder Path:</label>
                            <input
                                type="text"
                                id="documentFolderPath"
                                value={documentFolderPath}
                                onChange={(e) => setDocumentFolderPath(e.target.value)}
                                className="border p-2"
                            />
                        </div>
                    </>
                )}

                {searchType === 'searchByAuthorMD' && (
                    <div className="flex flex-col mb-2">
                        <label htmlFor="author" className="font-semibold">Author:</label>
                        <input
                            type="text"
                            id="author"
                            value={author}
                            onChange={(e) => setAuthor(e.target.value)}
                            className="border p-2"
                        />
                    </div>
                )}

                {searchType === 'searchByDateMD' && (
                    <div className="flex flex-col mb-2">
                        <label htmlFor="date" className="font-semibold">Date:</label>
                        <input
                            type="text"
                            id="date"
                            value={date}
                            onChange={(e) => setDate(e.target.value)}
                            className="border p-2"
                        />
                    </div>
                )}

                {searchType === 'searchByLanguageMD' && (
                    <div className="flex flex-col mb-2">
                        <label htmlFor="language" className="font-semibold">Language:</label>
                        <input
                            type="text"
                            id="language"
                            value={language}
                            onChange={(e) => setLanguage(e.target.value)}
                            className="border p-2"
                        />
                    </div>
                )}

                {searchType === 'fetchMD' && (
                    <div className="flex flex-col mb-2">
                        <label htmlFor="bookName" className="font-semibold">Book Name:</label>
                        <input
                            type="text"
                            id="bookName"
                            value={bookName}
                            onChange={(e) => setBookName(e.target.value)}
                            className="border p-2"
                        />
                    </div>
                )}

                <button type="submit" className="bg-blue-500 text-white p-2">Search</button>
            </form>

            <div>
                {results && (
                    <pre className="bg-gray-100 p-4 rounded">
                        {JSON.stringify(results, null, 2)}
                    </pre>
                )}
            </div>
        </div>
    );
};

export default Search;
