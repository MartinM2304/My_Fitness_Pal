#pragma once
#include "Pawn.h"
#include "Rook.h"
#include "Knight.h"
#include "Bishop.h"
#include "Queen.h"
#include "King.h"

class Board {
public:
	Piece* piece; //piece[32]
	Board();
	~Board();
};
