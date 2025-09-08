# 📈 Stock Market Tracker App

A modern Android application built using **MVVM architecture** with **Dagger Hilt**, **Retrofit**, and **MPAndroidChart**.  
The app integrates the **Alpha Vantage API** to fetch real-time and historical stock data, and visualizes it with **candlestick and line charts**.  
It features a sleek **black & yellow theme**, optimized network usage, and smooth navigation between screens.

---

## 🎥 Demo Video
👉 [Watch the Demo](#)  
_(Replace `#` with your actual demo video link once uploaded to YouTube or Google Drive)_

---

## 🚀 Features
- ⚡ **MVVM architecture** with clear separation of concerns.  
- 🛠 **Dagger Hilt** for dependency injection.  
- 🌐 **Retrofit** for internet API calls.  
- 📊 **MPAndroidChart** for:
  - **Candlestick chart** (daily & intraday movements).  
  - **Line chart** (historical trends).  
- 📡 **Alpha Vantage API Integration**:
  - Daily and Intraday endpoints.  
  - Custom parsing logic for chart datasets.  
- 🕒 **Multiple time filters supported**:
  - `1D`, `1W`, `15D`, `30D`, `3M`.  
- 🎨 **Modern black & yellow theme** for a professional look.  
- 📉 **Optimized API calls** → reduces network bandwidth by avoiding unnecessary requests.  
- ✅ **Error handling** with custom `Resource<T>` wrapper (Success, Error, Loading).  

---

## 📂 Project Structure

The project follows a modular, clean architecture approach with well-defined packages:


### 📌 Purpose of Each Folder
- **api** → Handles all network calls with Retrofit.  
- **constants** → Keeps API keys, URLs, and shared constants.  
- **database** → Stores offline data or caching (if used with Room).  
- **models** → Defines data models for API responses and entities.  
- **navigation** → Manages screen-to-screen navigation routes.  
- **screens** → UI implementation in Jetpack Compose.  
- **states** → Immutable state holders for screen data.  
- **utils** → Reusable helpers (e.g., date/time formatters, mappers).  
- **viewmodels** → Connects UI with repositories, ensures logic separation, and prevents unnecessary API calls.  

---

## 🛠️ Tech Stack
- **Kotlin** with **Coroutines & Flow**  
- **Jetpack Compose** (modern UI toolkit)  
- **MVVM** (Model-View-ViewModel architecture)  
- **Dagger Hilt** (dependency injection)  
- **Retrofit** (network layer)  
- **MPAndroidChart** (candlestick & line charts)  
- **Room** (if local database is needed)  

---

## 📡 API Integration

This app uses [Alpha Vantage API](https://www.alphavantage.co/) to fetch stock market data.

- **Intraday Endpoint** → Renders **candlestick chart** for short-term (real-time) movements.  
- **Daily Endpoint** → Renders **line chart** for long-term analysis.  

📊 Custom logic was written to transform API responses into datasets that MPAndroidChart can render efficiently for different time intervals (`1D`, `1W`, `15D`, `30D`, `3M`).

---

## 🎨 UI/UX
- Dark **black background** with **yellow highlights**.  
- Intuitive charts with zoom/pan support.  
- Smooth screen navigation.  
- Optimized data loading with visible loading states.  

---

## 🧑‍💻 Implementation Details
- **ViewModel segregation:**  
  Each feature/screen has its own ViewModel to avoid unnecessary API calls.  
- **Network optimization:**  
  Requests are cached or skipped if data is already available.  
- **State management:**  
  Every screen uses immutable state classes to ensure predictable UI rendering.  
- **Resource Wrapper:**  
  A `Resource<T>` sealed class is used to handle **Success, Loading, and Error** states cleanly.  



## 📌 Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/stock-market-tracker.git

🏆 Brownie Points

🌌 Modern black + yellow theme.

📊 Beautiful candlestick & line chart implementation.

🕒 Custom logic for time intervals (1D, 1W, 15D, 30D, 3M).

📉 Bandwidth-optimized API usage via ViewModel segregation.
   
