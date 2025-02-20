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
	void initPieces(int player, int pawnRow, int backRow);
	void init();
	void handleCommand(String command, int currentPlayer);
	void printHelp();
	void processMove(String command, int currentPlayer);
	//void undo(String move, Board board[8][8]);

private:
	bool isDestinationOccupiedByEnemy(Board board[8][8], int toNumber, int toLetter, int enemyPlayer)const;
	void handleCapture(Board board[8][8], int toNumber, int toLetter);
	bool isPathClear(Board board[8][8], int fromNumber, int fromLetter, int toNumber, int toLetter);
	void movePiece(Board board[8][8], int fromNumber, int fromLetter, int toNumber, int toLetter);
	int validateMoveLetter(char)const;
	int validateMoveNumber(char)const;

	bool validateMove(Board board[8][8], int fromLetter, int toLetter, int fromNumber, int toNumber, int currentPlayer);

	bool validatePawnMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer);
	bool validateKingMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer);
	bool validateQueenMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer);
	bool validateKnightMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer);
	bool validateBishopMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer);
	bool validateRookMove(Board board[8][8], int fromNumber, int toNumber, int fromLetter, int toLetter, int rowDifference, int columnDifference, int currentPlayer);

	char getPieceChar(String name, int player)const;

	
};


