#pragma once
#include "Piece.h"

class Rook :public Piece {
public:
	Rook() :Piece() {}
	Rook(const String _name, const int _player) :Piece(_name, _player) {}
};
