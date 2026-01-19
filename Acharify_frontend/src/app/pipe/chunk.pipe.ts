import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'chunk',
   standalone: true
})
export class ChunkPipe implements PipeTransform {
  transform<T>(array: T[], size: number): T[][] {
    if (!array || size <= 0) return [];

    const chunked: T[][] = [];
    for (let i = 0; i < array.length; i += size) {
      chunked.push(array.slice(i, i + size));
    }
    return chunked;
  }
}
