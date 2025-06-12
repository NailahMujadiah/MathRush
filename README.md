# MathRush – Aplikasi Edukasi Matematika Interaktif

**MathRush** adalah aplikasi edukasi berbasis Android yang dirancang untuk membantu pelajar meningkatkan kemampuan berhitung dasar lewat permainan yang interaktif dan menyenangkan.

## Fitur Utama

- Soal berdasarkan topik: Penjumlahan, Pengurangan, Perkalian, dan Pembagian  
- Level progression dengan tingkat kesulitan yang meningkat  
- Penyimpanan skor dan progress pengguna  
- Halaman profil untuk melihat username dan total skor  
- Navigasi antar halaman dengan fragment (Home, Level, Player, Profile)  

## Cara Penggunaan

1. Login atau Register terlebih dahulu  
2. Akses halaman **Home** dan pilih topik soal  
3. Pilih level, lalu kerjakan soal yang tersedia  
4. Lihat hasil dan progress di halaman **Profile**  

## Teknologi & Implementasi

- **Platform:** Android Studio  
- **Bahasa:** Java  
- **Arsitektur:** Fragment-based navigation  
- **Database:** SQLite melalui `AppDatabaseHelper`  
- **Data Soal:** Diambil dari file lokal JSON (`assets/questions.json`)  
- **Fitur UI:**  
  - Menggunakan `RecyclerView` untuk menampilkan daftar topik dan level  
  - Menggunakan `Fragment` untuk navigasi antar halaman  

## Informasi Kreator

- **Nama:** Nailah Mujadiah  
- **NIM:** H071231086  
