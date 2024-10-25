import React, { useState } from 'react';
import Sidebar from './Sidebar';
import Search from './Search';

const App = () => {
    const [searchType, setSearchType] = useState('searchWord');

    return (
        <div className="flex">
            <Sidebar onSelect={setSearchType} searchType={searchType} />
            <Search searchType={searchType} />
        </div>
    );
};

export default App;
