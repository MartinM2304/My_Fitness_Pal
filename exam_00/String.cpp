#include "String.h"

void String::addString(const String& other) {
    if (size == capacity) {
        resize();
    }
    if (capacity - size >= other.size) {
        size_t temp = size;
        for (int i = 0; i < other.size; i++) {
            str[temp] = other.str[i];
            ++temp;
        }
        size = size + other.size;
        str[size] = '\0';
    }
    else {
        resize();
        addString(other);
    }
}

void String::copy(const String& other) {
    capacity = other.capacity;
    size = other.size;
    str = new char[capacity + 1];

    for (int i = 0; i < size; i++) {
        str[i] = other.str[i];
    }
    str[size] = '\0';
}

void String::resize() {
    capacity = capacity * 2;
    char* temp = new char[capacity + 1];
    for (int i = 0; i < size; i++) {
        temp[i] = str[i];
    }
    delete[] str;
    str = temp;
}

void String::destroy() {
    delete[] this->str;
    // this->str = nullptr;
}

String::String() :str(nullptr), size(0), capacity(8) {}

String::String(const String& other) {
    copy(other);
}

String& String::operator=(const String& other) {
    if (this != &other) {
        destroy();
        copy(other);
    }
    return *this;
}

String::~String() {
    destroy();
}

String::String(const char* _str) {
    size = strlen(_str);
    capacity = size * 2;
    str = new char[capacity + 1];
    strcpy_s(str, capacity + 1, _str);
    str[size] = '\0';
}

size_t String::getLen() const {
    return size;
}

const bool String::isEmpty() const {
    return size == 0;
}

bool String::contains(String toFind)
{
    bool result = false;
    for (int i = 0; i < toFind.size; i++) {
        if (str[i] == toFind[i]) {
            for (int j = i; j < toFind.size + i; j++) {
                if (str[j] != toFind[j - i]) break;
                if (toFind.size - 1 == j - i) {
                    result = true;
                    break;
                }
            }

        }
    }

    return result;

}

char String::operator[](const size_t index) const {

    if (index >= this->size) {
        exit(-1);
    }
    return this->str[index];
}

void String::addChar(const char& add) {
    if (size == capacity - 1) {
        resize();
    }
    str[size] = add;
    ++size;
    str[size] = '\0';
}

void String::removeChar() {
    size--;
    str[size] = '\0';
}

String& String::operator+(const String& other) {
    addString(other);
    return*this;
}

String& String::operator+=(const String& other) {
    addString(other);
    return*this;
}

bool String::operator==(const String& other) {
    return strcmp(str, other.str) == 0 ? true : false;
}

std::ostream& operator<<(std::ostream& os, const String& _string) {
    for (int i = 0;i < _string.size;i++) {
        os << _string.str[i];
    }
    return os;
}

std::istream& operator>>(std::istream& is, String& _string) {
    char* buffer = new char[128];
    is.getline(buffer, 128);
    _string = String(buffer);
    delete[] buffer;

    return is;
}