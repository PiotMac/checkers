# checkers
A project for the Technologia Programowania course at the Wrocław University of Science and Technology.

Project contains working versions of three checkers variants:
  1. Thai checkers (8x8 board, black pieces move first, 8 pieces per team, flying kings, regular pieces can't capture backwards)
  2. English checkers (8x8 board, black pieces move first, 12 pieces per team, non-flying kings, regular pieces can't capture backwards, move ends once a regular piece reaches king's row)
  3. Shashki (8x8 board, white pieces move first, 12 pieces per team, flying kings, regular pieces able to capture backwards)

To play, first run a CheckersServerThread instance, then run two CheckersClient instances. The first instance will get the choice of variant, and first move.

Authors:
  Piotr Maciejończyk - client/server interaction, GUI
  Bartosz Tramś - logic (piece movement, checks for legal moves etc.)
