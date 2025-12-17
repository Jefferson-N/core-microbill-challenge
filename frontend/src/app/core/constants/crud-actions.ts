export const CRUD_ACTIONS = {
  UPDATE: 'update',
  DELETE: 'delete'
} as const;

export type CrudAction = typeof CRUD_ACTIONS[keyof typeof CRUD_ACTIONS];
