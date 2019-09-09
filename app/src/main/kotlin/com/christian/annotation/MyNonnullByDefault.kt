package com.christian.annotation

import java.lang.annotation.ElementType
import javax.annotation.meta.TypeQualifierDefault

@javax.annotation.Nonnull
@TypeQualifierDefault(ElementType.PARAMETER)
annotation class MyNonnullByDefault
