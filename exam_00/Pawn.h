#pragma once
#include "Piece.h"

class Pawn :public Piece {
public:
	Pawn() :Piece(){}
	Pawn(const String _name, const int _player) :Piece(_name, _player) {}
};
