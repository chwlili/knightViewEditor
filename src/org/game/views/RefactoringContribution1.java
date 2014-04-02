package org.game.views;

import java.util.Map;

import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringDescriptor;

public class RefactoringContribution1 extends RefactoringContribution
{

	public RefactoringContribution1()
	{
	}

	@Override
	public RefactoringDescriptor createDescriptor(String id, String project, String description, String comment, Map arguments, int flags) throws IllegalArgumentException
	{
		return null;
	}

}
