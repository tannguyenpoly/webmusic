-- 1. Tạo cơ sở dữ liệu
CREATE DATABASE webmusicai;
GO
USE webmusicai;
GO

-- 2. Bảng users (Quản lý người dùng)
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL, -- Dùng VARCHAR cho các trường không có tiếng Việt
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL, -- Đủ độ dài cho BCrypt
    full_name NVARCHAR(100), -- Dùng NVARCHAR cho tên có dấu
    avatar_url VARCHAR(255),
    token_balance INT DEFAULT 0,
    role VARCHAR(20) DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    is_active BIT DEFAULT 1, -- Khóa/Mở tài khoản
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE() -- Thêm field để track thời gian update
);
GO

-- 3. Bảng songs (Quản lý bài hát đã tạo)
CREATE TABLE songs (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT FOREIGN KEY REFERENCES users(id),
    title NVARCHAR(200) NOT NULL,
    prompt NVARCHAR(MAX), -- Lưu trữ câu lệnh prompt cấu hình
    audio_url VARCHAR(255), 
    cover_url VARCHAR(255), 
    duration_sec INT, 
    is_public BIT DEFAULT 0,
    token_cost INT,
    is_deleted BIT DEFAULT 0, -- Hỗ trợ xóa mềm (Soft Delete)
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);
GO

-- 4. Bảng token_packages (Cấu hình gói credit)
CREATE TABLE token_packages (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    token_amount INT NOT NULL,
    price DECIMAL(18,2) NOT NULL, -- Dùng 18,2 an toàn hơn cho tiền VNĐ
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);
GO

-- 5. Bảng token_transactions (Lịch sử nạp token/mua hàng)
CREATE TABLE token_transactions (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT FOREIGN KEY REFERENCES users(id),
    package_id BIGINT FOREIGN KEY REFERENCES token_packages(id),
    amount_paid DECIMAL(18,2), 
    token_amount INT, 
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED')),
    vnpay_txn_ref VARCHAR(100), 
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);
GO

-- 6. Bảng system_configs (Cấu hình hệ thống, lưu prompt của các nút chức năng)
CREATE TABLE system_configs (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_value NVARCHAR(MAX), 
    description NVARCHAR(255), 
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);
GO