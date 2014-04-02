package org.game.views;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.DeleteParticipant;

public class DeleteParticipant1 extends DeleteParticipant
{

	public DeleteParticipant1()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean initialize(Object element)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
