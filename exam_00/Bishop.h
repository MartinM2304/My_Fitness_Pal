#pragma once
#include "Piece.h"

class Bishop :public Piece {
public:
	Bishop() :Piece() {}
	Bishop(const String _name, const int _player) :Piece(_name, _player) {}
};
