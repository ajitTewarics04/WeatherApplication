import React, { useState } from 'react';
import axios from 'axios';

function App() {
const [city, setCity] = useState('');
const [forecast, setForecast] = useState(null);
const [error, setError] = useState('');

const fetchForecast = async () => {
try {
const response = await axios.get(`http://localhost:8080/api/v1/weather/forecast?city=${city}`);
setForecast(response.data);
setError('');
} catch (err) {
setForecast(null);
setError('❌ Could not fetch data. Try again later.');
}
};

return (
<div style={{ padding: '20px', fontFamily: 'Arial, sans-serif' }}>
<h1>🌤️ Weather Forecast</h1>

<div style={{ marginBottom: '20px' }}>
<input
type="text"
value={city}
placeholder="Enter city name"
onChange={e => setCity(e.target.value)}
style={{ padding: '6px', fontSize: '16px', width: '250px' }}
/>
<button onClick={fetchForecast} style={{ marginLeft: '10px', padding: '6px 12px' }}>
Get Forecast
</button>
</div>

{error && <p style={{ color: 'red' }}>{error}</p>}

{forecast && (
<div>
<h2>City: {forecast.city}</h2>
<ul>
{forecast.daily.map((day, index) => (
<li key={index} style={{ marginBottom: '12px' }}>
<strong>{day.date}</strong><br />
High: {day.high}°C, Low: {day.low}°C<br />
Advice: {day.advice.length > 0 ? day.advice.join(', ') : 'No advice'}
</li>
))}
</ul>
</div>
)}
</div>
);
}

export default App;