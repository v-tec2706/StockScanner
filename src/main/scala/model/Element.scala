package model

abstract class Element()

object Element {
  implicit def ElementToCompany(x: Element): Company = {
    x.asInstanceOf[Company]
  }

  implicit def ElementToIndex(x: Element): Index = {
    x.asInstanceOf[Index]
  }
}
