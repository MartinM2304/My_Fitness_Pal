#pragma once
#include "Piece.h"


class Queen : public Piece {
public:
	Queen() :Piece() {}
	Queen(const String _name, const int _player) :Piece(_name, _player) {}
};