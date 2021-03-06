/*
 *       >          
 *     <----       
 *   -------->                     
 *     <----      Flight 
 *       >        Copyright (c)2013 Zink Digital Ltd.    
 *      
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.  
 */

package com.zink.fly.examples

import com.zink.fly.{ Flight => flt }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._



case class Needle(val i : BigInt)

object NeedleInHaystack extends App {
  
  // start looking, before the needle is placed
  flt.read(Needle(2345), 100 seconds) onSuccess { case e => println(s"Found $e") }
  
  // write a few Needles in parallel 
  for (i <- (1 to 100000).par) flt.write(Needle(i), 10 seconds) 
  
  // take the needle out 
  flt.take(Needle(2345), 10 millis) onSuccess { case e => println("Taken it!") }
  // wait for trigger
  Thread.sleep( (100 millis).toMillis )
   
  // look again and its gone
  flt.read(Needle(2345), 10 millis) onFailure { case tbl => println("Daw missed it!") }
  
  // let the asyncs run 
  Thread.sleep( (100 millis).toMillis )
  sys.exit()
}
