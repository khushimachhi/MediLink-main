const express = require('express');
const cors = require('cors');
const dotenv = require('dotenv');
const mongoose = require("mongoose");

const authRouter = require("./routes/auth");
const userRouter = require("./routes/user");

// Load environment variables
dotenv.config();

// Debug: Check if env variables are loaded
console.log("MONGO_URI:", process.env.MONGO_URI);

const app = express();

// Connect to MongoDB
mongoose.connect(process.env.MONGO_URI, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
})
.then(() => {
  console.log("MongoDB connected successfully");
})
.catch(error => {
  console.error("MongoDB connection error:", error.message);
});

// Middleware
app.use(cors());
app.use(express.json());

// Test route
app.get('/greeting', (req, res) => {
  res.json({ message: 'Welcome to MediLink API!' });
});

// Routes
app.use("/v1/auth", authRouter);
app.use("/v1/user", userRouter);

// Error handling middleware
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({ message: 'Something went wrong!' });
});

// Start server
const PORT = process.env.PORT || 5000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});