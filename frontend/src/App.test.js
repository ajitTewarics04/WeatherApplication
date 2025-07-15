import { render, screen, fireEvent } from '@testing-library/react';
import App from './App';
import axios from 'axios';

jest.mock('axios');

test('renders input and button', () => {
render(<App />);
expect(screen.getByPlaceholderText(/enter city/i)).toBeInTheDocument();
expect(screen.getByText(/get forecast/i)).toBeInTheDocument();
});

test('fetches and displays forecast', async () => {
const mockData = {
data: {
city: 'London',
daily: [
{ date: '2025-07-15', high: 42, low: 28, advice: ['Use sunscreen lotion'] },
{ date: '2025-07-16', high: 30, low: 20, advice: [] }
]
}
};
axios.get.mockResolvedValueOnce(mockData);

render(<App />);
fireEvent.change(screen.getByPlaceholderText(/enter city/i), {
target: { value: 'London' }
});
fireEvent.click(screen.getByText(/get forecast/i));

expect(await screen.findByText(/London/)).toBeInTheDocument();
expect(await screen.findByText(/Use sunscreen lotion/)).toBeInTheDocument();
});