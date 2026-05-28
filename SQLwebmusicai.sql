-- 1. Tạo cơ sở dữ liệu
CREATE DATABASE webmusicai;
GO

USE webmusicai;
GO

-- 2. Tạo bảng users (Bảng cốt lõi)
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) UNIQUE NOT NULL,
    email NVARCHAR(100) UNIQUE NOT NULL,
    password_hash NVARCHAR(255) NOT NULL, -- BCrypt
    full_name NVARCHAR(100),
    avatar_url NVARCHAR(255), -- Cloudinary URL
    token_balance INT DEFAULT 0,
    role VARCHAR(10) CHECK (role IN ('USER', 'ADMIN')),
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE()
);
GO

-- 3. Tạo bảng songs (Phụ thuộc vào users)
CREATE TABLE songs (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT FOREIGN KEY REFERENCES users(id),
    title NVARCHAR(200) NOT NULL,
    prompt NVARCHAR(MAX), -- prompt dùng để tạo
    audio_url NVARCHAR(255), -- Cloudinary URL
    cover_url NVARCHAR(255), -- ảnh bìa (optional)
    duration_sec INT, -- thời lượng (giây)
    is_public BIT DEFAULT 0,
    token_cost INT, -- số token đã dùng
    created_at DATETIME2 DEFAULT GETDATE()
);
GO

-- 4. Tạo bảng token_packages (Bảng cấu hình gói token của admin)
CREATE TABLE token_packages (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    token_amount INT, -- số token nhận
    price DECIMAL(10,2), -- giá VNĐ
    is_active BIT DEFAULT 1
);
GO

-- 5. Tạo bảng token_transactions (Phụ thuộc vào users và token_packages)
CREATE TABLE token_transactions (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT FOREIGN KEY REFERENCES users(id),
    package_id BIGINT FOREIGN KEY REFERENCES token_packages(id),
    amount_paid DECIMAL(10,2), -- tiền thực tế
    token_amount INT, -- token nhận được
    status VARCHAR(10) CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED')),
    vnpay_txn_ref NVARCHAR(100), -- mã giao dịch VNPay
    created_at DATETIME2 DEFAULT GETDATE()
);
GO

-- 6. Tạo bảng system_configs (Bảng cấu hình hệ thống của admin)
CREATE TABLE system_configs (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    config_key NVARCHAR(100) UNIQUE NOT NULL,
    config_value NVARCHAR(MAX), -- JSON hoặc string
    description NVARCHAR(255), -- mô tả cho admin
    updated_at DATETIME2 DEFAULT GETDATE()
);
GO
