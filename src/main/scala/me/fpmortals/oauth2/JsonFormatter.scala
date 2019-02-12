package me.fpmortals.oauth2.jsonformat

import scalaz.IList

sealed abstract class JsValue

final case object JsNull extends JsValue
final case class JsObject(fields: IList[(String, JsValue)]) extends JsValue
final case class JsArray(elements: IList[JsValue]) extends JsValue
final case class JsBoolean(value: Boolean) extends JsValue
final case class JsString(value: String) extends JsValue
final case class JsDouble(value: Double) extends JsValue
final case class JsInteger(value: Integer) extends JsValue

