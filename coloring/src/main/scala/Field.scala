/**
 * Created by d.a.martyanov on 19.12.14.
 */
case class Field(
                  cells: Array[Array[Int]]
                  )

case class Cord(x: Int, y: Int)

object FieldProcessor {
  def make(n: Int, m: Int): Field = Field(Array.ofDim[Int](n, m))

  def setColor(fld: Field, c: Int, cord: Cord) =
    fld.copy(cells = fld.cells.updated(cord.x, fld.cells(cord.x).updated(cord.y, c)))
  
  def printField = fieldToStr andThen println

  def fieldToStr = (f: Field) => f.cells map (arr => arr.mkString(" ")) mkString ("\n")

  def update(f: Field, bms: Iterable[BotMove]) = {
    var newF = f
    bms foreach { move => newF = setColor(newF, move.color, move.cord) }  // вот тут мне совсем не нравится
    newF
  }
}

