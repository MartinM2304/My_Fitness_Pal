#pragma once
#include "Board.h"
#include "Pawn.h"
#include "Rook.h"
#include "Knight.h"
#include "Bishop.h"
#include "Queen.h"
#include "King.h"

class Gameplay {
public:
	Board board[8][8];
	char gameField[28][65];
	bool isKingDead = false;
	Gameplay();
	~Gameplay();
	void PlayGame();
	bool isMoveCommandValid(String move);

	bool canMove(String move, Board board[8][8], int currentPlayer);
	void FillBoard();
	void PaintBoard();
	void PrintBorders();
	void PrintNumbering();
	void FigureSpaceCleaning();
	void PlacePieces(Board board[8][8]);
	void PrintBoard(Board board[8][8]);
	//void undo(String move, Board board[8][8]);

private:
	int validateMoveLetter(char)const;
	int validateMoveNumber(char)const;

	bool validateMove(Board board[8][8], int fromLetter, int toLetter, int fromNumber, int toNumber, int currentPlayer)const;

	bool validatePawnPositions(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer)const;
	bool validatePawnMoveFirstPlayer(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference)const;
	bool validatePawnMoveSecondPlayer(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference)const;
	bool validatePawnMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer)const;
	bool validateKingMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer)const;
	bool validateQueenMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer)const;
	bool validateKnightMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer)const;
	bool validateBishopMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer)const;
	bool validateRookMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer)const;
};


