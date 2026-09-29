@echo off
chcp 65001 >nul
REM ============================================================
REM  สร้างไฟล์ NumberMaze.jar (ดับเบิลคลิกเล่นได้)
REM  วิธีใช้: ดับเบิลคลิกไฟล์นี้ หรือพิมพ์ build ใน Terminal
REM  ผลลัพธ์: dist\NumberMaze.jar
REM ============================================================
cd /d "%~dp0"

echo [1/4] ลบไฟล์เก่า...
if exist build rmdir /s /q build
if exist dist rmdir /s /q dist
mkdir build
mkdir dist

echo [2/4] Compile โค้ด (รองรับ Java 17 ขึ้นไป)...
javac -encoding UTF-8 --release 17 -d build *.java
if errorlevel 1 (
    echo Compile ไม่ผ่าน ดู error ด้านบน
    pause
    exit /b 1
)

echo [3/4] คัดลอกรูปภาพเข้าไปใน jar...
xcopy /e /i /q images build\images >nul

echo [4/4] สร้างไฟล์ jar...
REM  -e Main = ให้ jar เริ่มทำงานที่คลาส Main เมื่อดับเบิลคลิก
jar --create --file dist\NumberMaze.jar -e Main -C build .
if errorlevel 1 (
    echo สร้าง jar ไม่สำเร็จ
    pause
    exit /b 1
)

rmdir /s /q build
echo.
echo เสร็จแล้ว: dist\NumberMaze.jar
pause
