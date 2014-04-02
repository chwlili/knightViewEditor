package org.game.refactor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

public class MyRefactoring extends Refactoring
{

	@Override
	public String getName()
	{
		return null;
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm) throws CoreException, OperationCanceledException
	{
		return RefactoringStatus.createInfoStatus("Initial condition is OK!");
	}

	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm) throws CoreException, OperationCanceledException
	{
		return RefactoringStatus.createInfoStatus("Final condition is OK!");
	}

	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException
	{
		return null;
	}

}
