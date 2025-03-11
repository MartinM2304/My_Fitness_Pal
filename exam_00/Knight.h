#pragma once
#include "Piece.h"

class Knight :public Piece {
public:
	Knight() :Piece() {}
	Knight(const String _name,  const int _player) :Piece(_name, _player) {}
};