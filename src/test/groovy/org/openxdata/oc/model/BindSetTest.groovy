package org.openxdata.oc.model

import org.junit.Test

class BindSetTest extends GroovyTestCase{

	def bindSet = new BindSet()

	@Test void testBindSetContainsCorrectNumberOfItems(){

		bindSet.add("foo")
		bindSet.add("bar")

		assertEquals 2, bindSet.size()
		assertEquals 0, bindSet.duplicatedBindings.size()
	}

	@Test void testBindSetContainsCorrectTotalNumberOfItemsInTheSet(){

		bindSet.add("foo")
		bindSet.add("bar")
		bindSet.add("foo")
		bindSet.add("bar")

		assertEquals 2, bindSet.duplicatedBindings.size()
		assertEquals 4, bindSet.size() + bindSet.duplicatedBindings.size()
	}
}
