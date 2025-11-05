# Microservices Online Marketplace

Proyek ini adalah implementasi **Online Marketplace** dengan arsitektur **Microservices** menggunakan **Spring Boot 3**.  
Masing-masing service dibuat terpisah untuk memudahkan pengelolaan, pengembangan, dan deployment.

## 📌 Service yang Tersedia

- **User Service** → Manajemen user, autentikasi, otorisasi
- **Product Service** → Manajemen produk & kategori
- **Cart Service** → Manajemen keranjang belanja

---

## ✨ Fitur Utama

- 🔒 **JWT Authentication & Role-based Authorization** (Admin & Staff)
- 📧 **Integrasi SendGrid** → verifikasi email & notifikasi
- 📦 **Manajemen Produk** → CRUD produk & kategori
- 🛒 **Manajemen Cart** → Tambah, edit, hapus item keranjang
- 📖 **Swagger/OpenAPI Documentation**
- 🐳 **Docker Compose** → Containerization
- 🧪 **Unit Test dengan MockitoBean**

---

## 🚀 Cara Instalasi & Menjalankan

### 1. Clone Repository

```bash
git clone https://github.com/username/Microservice-Development-Challenge.git
cd Microservice-Development-Challenge
```

### 2. Jalankan dengan Docker Compose

Pastikan sudah menginstal Docker & Docker Compose.  
Jalankan perintah berikut:

```bash
docker-compose up --build
```

### 3. Akses Service

| Service | Port Container:Host | Deskripsi |
|---------|------------|-----------|
| user-service | 8084:8080 | Autentikasi & manajemen user |
| product-service | 8085:8081 | Manajemen produk & kategori |
| cart-service | 8086:8082 | Manajemen keranjang belanja |
| MySQL Database | 3307:3307 | Database untuk semua service |

---

## 📚 Struktur API Endpoint

### 🔐 User Service

| HTTP | Endpoint | Keterangan | Role |
|------|----------|------------|------|
| POST | `/auth/register` | Registrasi user baru | Public | 
| POST | `/auth/login` | Login user | Public |
| GET | `/auth/verify` | Verifikasi email | Public |
| GET | `/auth/user/{id}` | Ambil detail user by ID | Admin | 

### 📦 Product Service

#### 📌 Product Controller
| HTTP   | Endpoint                 | Keterangan          | Role   |
|--------|--------------------------|---------------------|--------|
| POST   | `/products/create`       | Tambah produk baru  | Admin  |
| PUT    | `/products/update/{id}`  | Update produk       | Admin  |
| GET    | `/products/get/{id}`     | Ambil detail produk | Public |
| GET    | `/products/get-all`      | List semua produk   | Public |
| DELETE | `/products/delete/{id}`  | Hapus produk        | Admin  |

#### 📌 Category Controller
| HTTP   | Endpoint               | Keterangan             | Role   |
|--------|------------------------|------------------------|--------|
| POST   | `/categories`          | Tambah kategori baru   | Admin  |
| GET    | `/categories`          | List semua kategori    | Public |
| GET    | `/categories/{id}`     | Ambil detail kategori  | Public |
| PUT    | `/categories/{id}`     | Update kategori        | Admin  |
| DELETE | `/categories/{id}`     | Hapus kategori         | Admin  |


### 🛒 Cart Service

| HTTP | Endpoint | Keterangan | Role | Service |
|------|----------|------------|------|---------|
| POST | `/cart/add` | Tambah produk ke cart | Staff | cart-service |
| PUT | `/cart/edit-cart` | Update item cart | Staff | cart-service |
| GET | `/cart` | Ambil isi cart | Staff | cart-service |
| DELETE | `/cart/delete/{productId}` | Hapus item dari cart | Staff | cart-service |

---

## 📧 Third-Party Integration

**SendGrid API** → Kirim email verifikasi saat registrasi user

---

## 🧪 Unit Testing

- Menggunakan **JUnit & MockitoBean**
- Service di-mock menggunakan `@MockitoBean`

---

## 📖 Dokumentasi API

Swagger UI tersedia di:

- **User Service** → http://localhost:8084/swagger-ui/index.html#/
- **Product Service** → http://localhost:8085/swagger-ui/index.html#/
- **Cart Service** → http://localhost:8086/swagger-ui/index.html#/

---

## 🛠 Teknologi yang Digunakan

- Java 21 + Spring Boot 3
- Spring Security + JWT
- Spring Data JPA + MySQL
- SendGrid (Email Service)
- Docker & Docker Compose
- Swagger / OpenAPI
- JUnit & Mockito

---

## 📌 License

Proyek ini dibuat untuk Microservice Development Challenge . 🚀
