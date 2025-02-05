interface RelationshipManagerInterface {
	// TODO define interface

	/**
	 * Returns a json segment that can be used to recreate this object.
	 *
	 * @return Json containing all nessisary state to recreate current state.
	 */
	public String toJson();
}
