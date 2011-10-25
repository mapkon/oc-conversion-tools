package org.openxdata.oc.model

class BindSet extends HashSet {
	
	final def duplicatedBindings = new ArrayList<String>()
	
	@Override
	public boolean add(String e) {
		if (contains(e)) {
			duplicatedBindings.add(e)
		}
		return super.add(e)
	}
}