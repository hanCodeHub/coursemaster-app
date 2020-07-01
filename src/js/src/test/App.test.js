import React from 'react';
import { render } from '@testing-library/react';
import App from '../components/App';

test('Add new user', () => {
  const { getByText } = render(<App />);
  const linkElement = getByText(/Add new user/i);
  expect(linkElement).toBeInTheDocument();
});
