#pragma once
#include "String.h"

class Piece {
private:
	String name;
	int player;
public:
	Piece();
	Piece(const String _name, const int _player);
	void setName(const String _name);
	void setPlayer(const int _player);
	String getName() const; 
	int getPlayer() const;
};
