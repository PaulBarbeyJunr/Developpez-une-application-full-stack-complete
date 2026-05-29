/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      colors: {
        mdd: {
          DEFAULT: '#6c5ce7',
          light: '#8d7bf0',
          dark: '#5a4bd4',
        },
      },
    },
  },
  plugins: [],
};
