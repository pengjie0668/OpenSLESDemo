#include "pcmdata.h"
#include "malloc.h"
#include "string.h"

pcmdata::pcmdata(char *data, int size) {

    this->data = (char *) malloc(size);
    this->size = size;
    memcpy(this->data, data, size);

}

pcmdata::~pcmdata() {

}

int pcmdata::getSize() {
    return size;
}

char *pcmdata::getData() {
    return data;
}

