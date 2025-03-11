#include "Piece.h"

Piece::Piece()
{
	name = "";
	player = 0;
}

Piece::Piece(const String _name,const int _player)
{
	name = _name;
	player = _player;
}

void Piece::setName(const String _name)
{
	name = _name;
}

void Piece::setPlayer(const int _player)
{
	player = _player;
}

String Piece:: getName() const
{
	return name;
}

int Piece::getPlayer() const
{
	return player;
}
