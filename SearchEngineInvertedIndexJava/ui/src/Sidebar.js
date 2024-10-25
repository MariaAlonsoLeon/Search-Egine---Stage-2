import React from 'react';

const Sidebar = ({ onSelect, searchType }) => {
    return (
        <div className="bg-gray-800 text-white w-64 min-h-screen">
            <h2 className="text-lg font-bold p-4">Search Options</h2>
            <ul className="space-y-2">
                <li>
                    <button
                        onClick={() => onSelect('searchWord')}
                        className={`w-full text-left p-2 ${searchType === 'searchWord' ? 'bg-gray-600' : 'hover:bg-gray-700'}`}
                    >
                        Search by Word
                    </button>
                </li>
                <li>
                    <button
                        onClick={() => onSelect('searchOr')}
                        className={`w-full text-left p-2 ${searchType === 'searchOr' ? 'bg-gray-600' : 'hover:bg-gray-700'}`}
                    >
                        Search OR
                    </button>
                </li>
                <li>
                    <button
                        onClick={() => onSelect('searchAnd')}
                        className={`w-full text-left p-2 ${searchType === 'searchAnd' ? 'bg-gray-600' : 'hover:bg-gray-700'}`}
                    >
                        Search AND
                    </button>
                </li>
                <li>
                    <button
                        onClick={() => onSelect('searchNot')}
                        className={`w-full text-left p-2 ${searchType === 'searchNot' ? 'bg-gray-600' : 'hover:bg-gray-700'}`}
                    >
                        Search NOT
                    </button>
                </li>
                <li>
                    <button
                        onClick={() => onSelect('paginate')}
                        className={`w-full text-left p-2 ${searchType === 'paginate' ? 'bg-gray-600' : 'hover:bg-gray-700'}`}
                    >
                        Paginated Search
                    </button>
                </li>
            </ul>
        </div>
    );
};

export default Sidebar;
