#!/data/data/com.termux/files/usr/bin/bash
set -e
echo "[+] Compilando poc.cpp..."
clang++ app/src/main/cpp/poc.cpp -o $HOME/poc -g -fsanitize=address -fno-omit-frame-pointer
echo "[+] Ejecutando..."
$HOME/poc
~/citadel_poc_project $ cat run_poc.shm
chmod +x run_poc.sh
