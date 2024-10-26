import React, { useState } from 'react';

const Search = ({ searchType, onSearch }) => {
    const [word1, setWord1] = useState('');
    const [word2, setWord2] = useState('');
    const [pageSize, setPageSize] = useState(10);
    const [pageNumber, setPageNumber] = useState(1);
    const [results, setResults] = useState(null);

    const handleSubmit = (e) => {
        e.preventDefault();
        let url = '';

        const BASE_URL = 'http://localhost:8080';

        switch (searchType) {
            case 'searchWord':
                url = `${BASE_URL}/search/${word1}`;
                break;
            case 'searchOr':
                url = `${BASE_URL}/search/or/${word1}/${word2}`;
                break;
            case 'searchAnd':
                url = `${BASE_URL}/search/and/${word1}/${word2}`;
                break;
            case 'searchNot':
                url = `${BASE_URL}/search/not/${word1}`;
                break;
            case 'paginate':
                url = `${BASE_URL}/search/paginate/${word1}/${pageSize}/${pageNumber}`;
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
                <div className="flex flex-col mb-2">
                    <label htmlFor="word1" className="font-semibold">Word 1:</label>
                    <input type="text" id="word1" value={word1} onChange={(e) => setWord1(e.target.value)} className="border p-2" />
                </div>
                {(searchType === 'searchOr' || searchType === 'searchAnd') && (
                    <div className="flex flex-col mb-2">
                        <label htmlFor="word2" className="font-semibold">Word 2:</label>
                        <input type="text" id="word2" value={word2} onChange={(e) => setWord2(e.target.value)} className="border p-2" />
                    </div>
                )}
                {searchType === 'paginate' && (
                    <>
                        <div className="flex flex-col mb-2">
                            <label htmlFor="pageSize" className="font-semibold">Page Size:</label>
                            <input type="number" id="pageSize" value={pageSize} onChange={(e) => setPageSize(e.target.value)} className="border p-2" />
                        </div>
                        <div className="flex flex-col mb-2">
                            <label htmlFor="pageNumber" className="font-semibold">Page Number:</label>
                            <input type="number" id="pageNumber" value={pageNumber} onChange={(e) => setPageNumber(e.target.value)} className="border p-2" />
                        </div>
                    </>
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
