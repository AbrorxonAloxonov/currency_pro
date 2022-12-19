import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './currency.reducer';

export const CurrencyDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const currencyEntity = useAppSelector(state => state.currency.entity);
  const updateSuccess = useAppSelector(state => state.currency.updateSuccess);

  const handleClose = () => {
    navigate('/currency' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(currencyEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="currencyDeleteDialogHeading">
        O`chirish amalini tasdiqlang
      </ModalHeader>
      <ModalBody id="currencyProApp.currency.delete.question">{currencyEntity.id}- Currencyni o`chirmoqchimisiz?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Bekor
        </Button>
        <Button id="jhi-confirm-delete-currency" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; O`chir
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default CurrencyDeleteDialog;
