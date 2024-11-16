import React from 'react';

const Sidebar = ({ onSelect, searchType }) => {
    return (
        <div className="bg-gray-800 text-white w-64 min-h-screen">
            <h2 className="text-lg font-bold p-4">Search Options</h2>
            <ul className="space-y-2">
                <li>
                    <button
                        onClick={() => onSelect('searchSingleWordII')}
                        className={`w-full text-left p-2 ${searchType === 'searchSingleWordII' ? 'bg-gray-600' : 'hover:bg-gray-700'}`}
                    >
                        Search by Word
                        <span className="ml-2 inline-flex items-center justify-center w-6 h-6 bg-blue-500 text-white rounded-full text-xs">II</span>
                    </button>
                </li>
                <li>
                    <button
                        onClick={() => onSelect('searchWithContextII')}
                        className={`w-full text-left p-2 ${searchType === 'searchWithContextII' ? 'bg-gray-600' : 'hover:bg-gray-700'}`}
                    >
                        Search with Context
                        <span className="ml-2 inline-flex items-center justify-center w-6 h-6 bg-blue-500 text-white rounded-full text-xs">II</span>
                    </button>
                </li>
                <li>
                    <button
                        onClick={() => onSelect('searchByAuthorMD')}
                        className={`w-full text-left p-2 ${searchType === 'searchByAuthorMD' ? 'bg-gray-600' : 'hover:bg-gray-700'}`}
                    >
                        Search by Author
                        <span className="ml-2 inline-flex items-center justify-center w-6 h-6 bg-green-500 text-white rounded-full text-xs">MD</span>
                    </button>
                </li>
                <li>
                    <button
                        onClick={() => onSelect('searchByDateMD')}
                        className={`w-full text-left p-2 ${searchType === 'searchByDateMD' ? 'bg-gray-600' : 'hover:bg-gray-700'}`}
                    >
                        Search by Date
                        <span className="ml-2 inline-flex items-center justify-center w-6 h-6 bg-green-500 text-white rounded-full text-xs">MD</span>
                    </button>
                </li>
                <li>
                    <button
                        onClick={() => onSelect('searchByLanguageMD')}
                        className={`w-full text-left p-2 ${searchType === 'searchByLanguageMD' ? 'bg-gray-600' : 'hover:bg-gray-700'}`}
                    >
                        Search by Language
                        <span className="ml-2 inline-flex items-center justify-center w-6 h-6 bg-green-500 text-white rounded-full text-xs">MD</span>
                    </button>
                </li>
                <li>
                    <button
                        onClick={() => onSelect('fetchMD')}
                        className={`w-full text-left p-2 ${searchType === 'fetchMD' ? 'bg-gray-600' : 'hover:bg-gray-700'}`}
                    >
                        Fetch Book
                        <span className="ml-2 inline-flex items-center justify-center w-6 h-6 bg-green-500 text-white rounded-full text-xs">MD</span>
                    </button>
                </li>
            </ul>
        </div>
    );
};

export default Sidebar;
