#include <iostream>
#include <cstring>
int main(){
    size_t dest_size = 16;
    uint8_t* dest = new uint8_t[dest_size];
    uint8_t src[32];
    memset(src, 0x41, 32);
    int declared = 2;
    size_t header = 4;
    size_t payload = declared - header; // underflow -> gigante
    std::cout << "payloadLength = " << payload << "\n";
    std::cout << "Copiando...\n";
    memcpy(dest, src, payload); // aqui truena Scudo/ASAN
    delete[] dest;
    return 0;
}
