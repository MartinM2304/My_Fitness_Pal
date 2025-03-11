#pragma once
#include "Piece.h"

class King :public Piece {
public:
	King() :Piece() {}
	King(const String _name, const int _player) :Piece(_name,  _player) {}
};