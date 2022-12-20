import dayjs from 'dayjs';

export interface ICurrency {
  id?: number;
  code?: string | null;
  ccy?: string | null;
  ccyNmRU?: string | null;
  ccyNmUZ?: string | null;
  ccyNmUZC?: string | null;
  ccyNmEN?: string | null;
  nominal?: string | null;
  rate?: string | null;
  diff?: string | null;
  date?: string | null;
}

export const defaultValue: Readonly<ICurrency> = {};
