package list

import realization.types.comporators.Comporator

import java.util
import java.util.{Arrays, Comparator}
import scala.annotation.tailrec
import scala.util.control.Breaks.break

class MyListOfArrays {
  private var sizeOfArrays = 0 // размерность каждого массива

  private var size = 0 // Размер списка

  private var totalElements = 0 // Количество элементов во всех массивах


  private var head: Node = null
  private var tail: Node = null

  def this(sizeOfArrays: Int) {
    this()
    this.sizeOfArrays = Math.sqrt(sizeOfArrays).toInt
    //this.cidx = 1; // не пустая = 1 линейный массив
    this.size = 1
    this.head = new Node(this.sizeOfArrays)
    this.tail = this.head
    this.totalElements = 0
  }

  /**
   * Добавить элемент в конец всей структуры
   *
   * @param value
   */
  def add(value: AnyRef): Unit = {
    val current = this.tail
    current.addElementOnArray(value)
    if (current.getCountOfElementsInArray == this.sizeOfArrays) {
      val kk1 = this.sizeOfArrays * 3 / 4 // 75% в старом

      val kk2 = this.sizeOfArrays - kk1 // 25% в новом

      // Новое количество элементов в старом массиве
      current.setCountOfElementsInArray(kk1)
      // Создаём новый узел и ставим ему количество элементов в массиве
      val newNode = new Node(this.sizeOfArrays)
      current.next = newNode
      newNode.prev = current
      this.tail = newNode
      newNode.setCountOfElementsInArray(kk2)
      // Копируем 25% данных в новый узел из предыдущего узла
      for (i <- 0 until kk2) {
        newNode.array(i) = current.array(kk1 + i)
        current.array(kk1 + i) = null
      }
      // Увеличиваем счётчик узлов списка
      this.size += 1
    }
    this.totalElements += 1
  }

  def get(logicalIndexOfElement: Int): AnyRef = {
    try {
      val physicalIndex = getIndex(logicalIndexOfElement)
      if (physicalIndex(0) == -1 || physicalIndex(1) == -1) throw new IndexOutOfBoundsException("Error. Index out of bounds")
      val current = getNode(physicalIndex(0))
      return current.array(physicalIndex(1))
    } catch {
      case error: IndexOutOfBoundsException =>
        System.out.println(error.getMessage)
    }
    null
  }

  /**
   * Преобразование логического номера в физический
   *
   * @param logicalIndexOfElement
   * @return Возвращает массив из двух элементов:
   *         1 - индекс узла
   *         2 - физический индекс элемента
   */
  private def getIndex(logicalIndexOfElement: Int): Array[Int] = {
    // Обработка неверного значения
    if (logicalIndexOfElement < 0 || logicalIndexOfElement >= this.totalElements) return Array[Int](-1, -1)
    // Преобразование в физический индекс
    var tmp = head
    var indexNode = 0
    var physicalIndexOfElement = logicalIndexOfElement
    indexNode = 0
    while (tmp != null) {
      if (physicalIndexOfElement < tmp.getCountOfElementsInArray) return Array[Int](indexNode, physicalIndexOfElement)
      physicalIndexOfElement -= tmp.getCountOfElementsInArray
      tmp = tmp.next

      indexNode += 1
    }
    Array[Int](-1, -1)
  }

  /**
   * Вспомогательный метод поиска узла
   *
   * @param index
   * @return
   */
  private def getNode(index: Int): Node = {
    try {
      if (index < 0 || index >= this.size) throw new IndexOutOfBoundsException
      if (index == 0) return this.head
      var tmp = this.head
      for (i <- 0 until index) {
        tmp = tmp.next
      }
      return tmp
    } catch {
      case ex: IndexOutOfBoundsException =>
        System.out.println("Error. Out of bounds list")
    }
    null
  }

  /**
   * Вставка по логическому индексу в структуру
   *
   * @param value
   * @param logicalIndexOfElement
   * @return
   */
  def insert(value: AnyRef, logicalIndexOfElement: Int): Int = {
    try {
      val physicalIndex = getIndex(logicalIndexOfElement)
      if (physicalIndex(0) == -1 || physicalIndex(1) == -1) throw new IndexOutOfBoundsException("Error. Index out of bounds")
      val current = getNode(physicalIndex(0))
      // Раздвижка в массиве
      for (i <- current.getCountOfElementsInArray - 1 to physicalIndex(1) by -1) {
        current.array(i + 1) = current.array(i)
      }
      // Вставка нового элемента
      current.array(physicalIndex(1)) = value
      current.setCountOfElementsInArray(current.getCountOfElementsInArray + 1)
      this.totalElements += 1
      // В случае переполнения -> раздвижка списка
      if (current.getCountOfElementsInArray == this.sizeOfArrays) {
        val newNode = new Node(this.sizeOfArrays)
        // Перекидываем указатели
        newNode.prev = current
        newNode.next = current.next
        if (current.next == null) {
          // Если current это последний узел списка
          this.tail = newNode
        }
        else {
          // Если за current ещё есть узел
          current.next.prev = newNode
        }
        current.next = newNode
        // Перенос половины current в новый узел
        current.setCountOfElementsInArray(this.sizeOfArrays / 2)
        newNode.setCountOfElementsInArray(this.sizeOfArrays - current.getCountOfElementsInArray)
        val countElementsOfNewNode = newNode.getCountOfElementsInArray
        for (i <- 0 until countElementsOfNewNode) {
          newNode.array(i) = current.array(this.sizeOfArrays / 2 + i)
          current.array(this.sizeOfArrays / 2 + i) = null
        }
        this.size += 1
      }
      return 1
    } catch {
      case error: IndexOutOfBoundsException =>
        System.out.println(error.getMessage)
    }
    0
  }

  def remove(logicalIndexOfElement: Int): Int = {
    try {
      val physicalIndex = getIndex(logicalIndexOfElement)
      if (physicalIndex(0) == -1 || physicalIndex(1) == -1) throw new IndexOutOfBoundsException("Error. Index out of bounds")
      val current = getNode(physicalIndex(0))
      // Сдвиг в массиве
      current.array(physicalIndex(1)) = null
      for (i <- physicalIndex(1) until current.getCountOfElementsInArray) {
        current.array(i) = current.array(i + 1)
      }
      this.totalElements -= 1
      current.setCountOfElementsInArray(current.getCountOfElementsInArray - 1)
      // Если массив оказался пустым, то удалим узел перекинув указатели
      if (current.getCountOfElementsInArray == 0) {
        if (current.prev != null) if (current.next != null) current.prev.next = current.next
        else {
          current.prev.next = null
          this.tail = current.prev
        }
        if (current.next != null) if (current.prev != null) current.next.prev = current.prev
        else {
          current.next.prev = null
          this.head = current.next
        }
        if (this.size != 1) this.size -= 1
      }
      return 1
    } catch {
      case error: IndexOutOfBoundsException =>
        System.out.println(error.getMessage)
    }
    0
  }



  def forEach(a: Any => Unit): Unit = {
    var tmp: Node = this.head
    while (tmp != null) {
      a(tmp.array)
      tmp = tmp.next
    }
  }

  def clear(): Unit = {
    this.size = 0
    this.totalElements = 0
    this.head = null
    this.tail = null
  }


  def show(): Unit = {
    var tmp: Node = this.head
    var numOfCurrentNode: Int = 0
    while (tmp != null) {
      System.out.println(numOfCurrentNode + ": " + util.Arrays.toString(tmp.array))
      numOfCurrentNode += 1
      tmp = tmp.next
    }
  }


  def getSize: Int = size

  def getTotalElements: Int = totalElements

  def getSizeOfArrays: Int = this.sizeOfArrays

  override def toString: String = {
    var str: String = ""
    var elements: String = ""
    var indexNode: Int = 0
    var tmp: Node = head
    while (tmp != null) {
      for (j <- 0 until tmp.getCountOfElementsInArray) {
        if (tmp.array(j) != null) elements += tmp.array(j).toString + " "
      }
      str += indexNode + ": " + "[" + elements + "]\n"
      tmp = tmp.next
      indexNode += 1
      elements = ""
    }
    str
  }


  def mergeSortFunc(arr: Array[AnyRef], comporator: Comporator): Array[AnyRef] = {
    if (arr.length <= 1) {
      arr
    } else {
      val (left, right) = arr.splitAt(arr.length / 2)
      mergeFunc(mergeSortFunc(left, comporator), mergeSortFunc(right, comporator), comporator)
    }
  }

  def mergeFunc(left: Array[AnyRef], right: Array[AnyRef], comporator: Comporator): Array[AnyRef] = {
    (left, right) match {
      // Если один из массивов пуст, мы просто возвращаем другой массив
      case (left, right) if left.isEmpty => right
      case (left, right) if right.isEmpty => left
      case (left, right) =>
        // В противном случае мы сравниваем первые элементы обоих массивов и добавляем меньший элемент к результату
        if (comporator.compare(left.head, right.head) < 0) left.head +: mergeFunc(left.tail, right, comporator)
        else right.head +: mergeFunc(left, right.tail, comporator)
    }
  }

  def sortFuncStyle(comporator: Comporator): MyListOfArrays = {

    var arrayOfAllElements: Array[AnyRef] = arraysInOneArray()

    arrayOfAllElements = mergeSortFunc(arrayOfAllElements, comporator)

    // Создаём новый список
    var newList: MyListOfArrays = new MyListOfArrays(Math.pow(sizeOfArrays, 2).toInt)
    arrayOfAllElements.foreach(item => {
      newList.add(item)
    })

    newList
  }


  /**
   * Разбиение массива на части
   *
   * @param arr
   * @param comporator
   */
  private def mergeSortArray(arr: Array[AnyRef], comporator: Comporator, countElem: Int): Unit = {
    val arrSize = countElem
    if (arrSize == 1) return
    val middle = arrSize / 2
    val left = new Array[AnyRef](middle)
    val right = new Array[AnyRef](arrSize - middle)
    for (i <- 0 until middle) {
      left(i) = arr(i)
    }
    for (i <- 0 until arrSize - middle) {
      right(i) = arr(middle + i)
    }
    mergeSortArray(left, comporator, left.length)
    mergeSortArray(right, comporator, right.length)
    mergeArrays(arr, left, right, comporator)
  }

  /**
   * Слияние подмассивов
   *
   * @param arr        результирующий массив
   * @param leftArr    первый подмассив
   * @param rightArr   второй подмассив
   * @param comporator объект для сравнения значений пользовательских типов данных
   */
  private def mergeArrays(arr: Array[AnyRef], leftArr: Array[AnyRef], rightArr: Array[AnyRef], comporator: Comporator): Unit = {
    val leftSize = leftArr.length
    val rightSize = rightArr.length
    var i = 0
    var j = 0
    var idx = 0
    while (i < leftSize && j < rightSize) {
      if (comporator.compare(leftArr(i), rightArr(j)) < 0) {
        arr(idx) = leftArr(i)
        i += 1
      }
      else {
        arr(idx) = rightArr(j)
        j += 1
      }
      idx += 1
    }
    // Если размеры массивов были разными,
    // то добавляем в результирующий массив остатки
    for (ll <- i until leftSize) {
      arr({
        idx += 1;
        idx - 1
      }) = leftArr(ll)
    }
    for (rr <- j until rightSize) {
      arr({
        idx += 1;
        idx - 1
      }) = rightArr(rr)
    }

  }

  def sort(comporator: Comporator): MyListOfArrays = {
    // Переносим все элементы в один массив
    var arrayOfAllElements: Array[AnyRef] = arraysInOneArray()

    mergeSortArray(arrayOfAllElements, comporator, arrayOfAllElements.length)

    // Создаём новый список
    var newList: MyListOfArrays = new MyListOfArrays(Math.pow(sizeOfArrays, 2).toInt)
    arrayOfAllElements.foreach(item => {
      newList.add(item)
    })

    newList
  }


  def arraysInOneArray(): Array[AnyRef] = {
    // Переносим все элементы в один массив
    var arrayOfAllElements: Array[AnyRef] = new Array[AnyRef](totalElements)
    for (i <- 0 until totalElements) {
      arrayOfAllElements(i) = this.get(i)
    }

    arrayOfAllElements
  }

  private class Node {
    var array: Array[AnyRef] = _

    var countOfElementsInArray = 0
    var next: Node = _
    var prev: Node = _

    def this(size: Int) {
      this()
      this.array = new Array[AnyRef](size)
      this.countOfElementsInArray = 0
      this.next = null
      this.prev = null
    }

    def addElementOnArray(value: AnyRef): Unit = {
      this.array(countOfElementsInArray) = value
      this.countOfElementsInArray += 1
    }

    def getCountOfElementsInArray: Int = countOfElementsInArray

    def setCountOfElementsInArray(countOfElementsInArray: Int): Unit = {
      this.countOfElementsInArray = countOfElementsInArray
    }

    override def toString: String = util.Arrays.toString(array)
  }
}

