
# Completed Coupon Project

The Completed Coupon Project is a full-stack application designed to manage and utilize various coupons efficiently. This system provides a user-friendly interface for multiple roles including customers, admins, companies, and guests. The project leverages JWT for secure authentication and follows RESTful API principles.

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Installation

1. **Clone the repository**
   ```sh
   git clone git@github.com:moradi12/Completed-Coupon-Project.git
   ```

2. **Navigate to the project directory**
   ```sh
   cd Completed-Coupon-Project-master
   ```

3. **Frontend Setup**
   - Navigate to the frontend directory
     ```sh
     cd CouponsWebsite
     ```
   - Install dependencies
     ```sh
     npm install
     ```
   - Start the frontend server
     ```sh
     npm start
     ```

4. **Backend Setup**
   - Navigate to the backend directory
     ```sh
     cd final_project-master
     ```
   - Install dependencies and set up the environment
     ```sh
     npm install
     # Set up environment variables as needed
     ```
   - Start the backend server
     ```sh
     npm start
     ```

## Usage

- The frontend of the project is served at `http://localhost:3000`
- The backend API is available at `http://localhost:5000`

### Main Features
- **Coupon Management**: Easily create, edit, delete, and view coupons.
- **User Authentication**: Secure login and registration functionalities with JWT support.
- **Admin Dashboard**: A robust interface for managing users, coupons, and system settings.
- **User Roles**: The system supports multiple roles, including:
  - **Customers**: Users who can view and utilize coupons.
  - **Admins**: Users with privileges to manage the system and users.
  - **Companies**: Entities that can create and manage their own coupons.
  - **Guests**: Users who can browse the available coupons without an account.

This project demonstrates a comprehensive understanding of modern web development technologies, including React, Node.js, Express, and JWT for secure authentication. It's designed to be scalable, efficient, and user-friendly, following RESTful API principles.

## Contributing

Contributions are welcome! Follow these steps to contribute:

1. Fork the project
2. Create your feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -m 'Add SomeFeature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

## License

Distributed under the MIT License. See `LICENSE` for more information.

## Acknowledgments

- **Libraries**: This project utilizes [React](https://reactjs.org/), [Node.js](https://nodejs.org/), [Express](https://expressjs.com/), and other modern web technologies.
- **Special Thanks**: Special thanks to all contributors and those who provided feedback during the development of this project.
